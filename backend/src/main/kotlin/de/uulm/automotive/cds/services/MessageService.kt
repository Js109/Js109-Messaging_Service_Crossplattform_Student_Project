package de.uulm.automotive.cds.services

import com.rabbitmq.client.AMQP
import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.entities.MessageSerializable
import de.uulm.automotive.cds.models.dtos.MessageDisplayPropertiesDTO
import de.uulm.automotive.cds.repositories.MessageRepository
import org.hibernate.Hibernate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.chrono.ChronoLocalDateTime
import java.util.*
import kotlin.collections.HashMap

/**
 * A service class that takes care of sending messages via the amqp broker.
 * Relies on AmqpChannelService.
 *
 * @property amqpChannelService AmqpChannelService component used to communicate with the broker.
 */
@Component

class MessageService @Autowired constructor(val amqpChannelService: AmqpChannelService, val messageRepository: MessageRepository) {

    /**
     * Publishes a messages to the broker.
     * If the message passed contains properties the topic will be ignored and header exchange will be used.
     * Otherwise topic exchange with the topic of the message will be used.
     * @param message The message to be published by the broker. If it has properties set those will be used for publishing, otherwise the topic of the message will be used for publishing.
     */
    fun sendMessage(message: Message) {
        val channel = amqpChannelService.openChannel()
        val messageSerializable =
                MessageSerializable(
                        message.sender!!,
                        message.title!!,
                        message.content,
                        message.attachment,
                        message.logoAttachment,
                        message.links?.toTypedArray(),
                        message.locationData?.serialize(),
                        message.endtime,
                        message.messageDisplayProperties?.serialize()
                )

        val properties = AMQP.BasicProperties.Builder()
        message.endtime?.millisFromCurrentTime()
                ?.also { if (it < 0) return }
                ?.let { addExpirationToProps(properties, it) }

        if (message.properties == null || message.properties?.size == 0) {
            channel.basicPublish("amq.topic", message.topic, properties.build(), messageSerializable.toByteArray())
        } else {
            addHeaderProps(properties, message.properties)
            channel.basicPublish("amq.headers", "", properties.build(), messageSerializable.toByteArray())
        }

        channel.close()
    }

    /**
     * Calculates the amount of milliseconds between the given LocalDateTime and the current time
     */
    private fun LocalDateTime.millisFromCurrentTime(): Long? {
        return this.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?.minus(System.currentTimeMillis())
    }

    /**
     * Adds an expiration date to the given AMQP property builder based on the current time and the given LocalDateTime if it is not noll null or in the past.
     * @param properties AMQP.BasicProperties.Builder to which the expiration will be added
     * @param expiration Duration in Long after which the message should not be send any more
     */
    private fun addExpirationToProps(properties: AMQP.BasicProperties.Builder, expiration: Long) {
        properties.expiration(expiration.toString())
    }

    /**
     * Adds the appropriate headers for message publishing on the header exchange based on the message properties to the given AMQP properties builder.
     * @param amqpPropertiesBuilder AMQP.BasicProperties.Builder to which the headers will be added
     * @param properties List of properties from the message to be published from which the headers are created
     */
    private fun addHeaderProps(amqpPropertiesBuilder: AMQP.BasicProperties.Builder, properties: List<String>?) {
        val bindingArgs = HashMap<String, Any>()
        properties?.forEach {
            bindingArgs[it] = ""
        }
        amqpPropertiesBuilder.headers(bindingArgs)
    }

    /**
     * This method filters the message-list by their sending timestamp.
     * The transactional context is required to be able to lazy load only the properties and links of messages,
     * which should be sent.
     *
     * @return List of messages which should be processed (sent)
     */
    @Transactional
    fun filterCurrentMessages(): List<Message> {
        val messages = messageRepository.findAllByIsSentFalseOrderByStarttimeAsc()
        return messages.filter { message: Message ->
            val starttime = message.starttime
            starttime == null || starttime < LocalDateTime.now()
        }.apply {
            forEach { message ->
                // triggers the loading of the lazy-fetch attributes
                Hibernate.initialize(message.links)
                Hibernate.initialize(message.properties)
            }
        }
    }
}