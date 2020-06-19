package de.uulm.automotive.cds.services

import com.rabbitmq.client.AMQP
import de.uulm.automotive.cds.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * TODO
 *
 * @property amqpChannelService
 */
@Component
@Transactional
class MessageService @Autowired constructor(val amqpChannelService: AmqpChannelService) {

    /**
     * Publishes a messages to the broker.
     * If the message passed contains properties the topic will be ignored and header exchange will be used.
     * Otherwise topic exchange with the topic of the message will be used.
     * @param message The message to be published by the broker. If it has properties set those will be used for publishing, otherwise the topic of the message will be used for publishing.
     */
    fun sendMessage(message: Message) {
        val channel = amqpChannelService.openChannel()

        if (message.properties == null || message.properties?.size == 0) {
            channel.basicPublish("amq.topic", message.topic, null, message.content.toByteArray())
        } else {
            val properties = createHeaderProps(message.properties)
            channel.basicPublish("amq.headers", "", properties, message.content.toByteArray())
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
}