package de.uulm.automotive.cds.services

import com.rabbitmq.client.AMQP
import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.entities.MessageSerializable
import de.uulm.automotive.cds.repositories.MessageRepository
import org.hibernate.Hibernate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

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
        val messageSerializable = MessageSerializable(message.sender!!, message.title!!, message.content, message.attachment, message.links?.toTypedArray(), message.locationData?.serialize())

        if (message.properties == null || message.properties?.size == 0) {
            channel.basicPublish("amq.topic", message.topic, null, messageSerializable.toByteArray())
        } else {
            val properties = createHeaderProps(message.properties)
            channel.basicPublish("amq.headers", "", properties, messageSerializable.toByteArray())
        }

        channel.close()
    }

    /**
     * Creates the appropriate headers for message publishing on the header exchange from the properties of a message.
     * @param properties List of properties from the message to be published
     * @return BasicProperties for amqp publishing containing the headers
     */
    private fun createHeaderProps(properties: List<String>?): AMQP.BasicProperties {
        val bindingArgs = HashMap<String, Any>()
        properties?.forEach {
            bindingArgs[it] = ""
        }
        return AMQP.BasicProperties().builder().headers(bindingArgs).build()
    }

    /**
     * This method filters the message-list by their sending timestamp.
     * The transactional context is required to be able to lazy load only the properties and links of messages,
     * which should be sent.
     *
     * @return List of messages which should be processed (sent)
     */
    @Transactional
    fun filterCurrentMessages(): List<Message>{
        val messages = messageRepository.findAllByIsSentFalseOrderByStarttimeAsc()
        return messages.filter { message: Message ->
            message.starttime!! < LocalDateTime.now()
        }.apply {
            forEach { message ->
                // triggers the loading of the lazy-fetch attributes
                Hibernate.initialize(message.links)
                Hibernate.initialize(message.properties)
            }
        }
    }
}