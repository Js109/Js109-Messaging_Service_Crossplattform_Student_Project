package de.uulm.automotive.cds.services

import com.rabbitmq.client.AMQP
import de.uulm.automotive.cds.entities.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.ResourceUtils
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

/**
 * A service class that takes care of sending messages via the amqp broker.
 * Relies on AmqpChannelService.
 *
 * @property amqpChannelService AmqpChannelService component used to communicate with the broker.
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
    fun sendMessage(message: Message, attachment: MultipartFile?) {
        val channel = amqpChannelService.openChannel()

        /*val x = ClassPathResource("images\\logo.jpg").file
        val bImage = ImageIO.read(x)
        val byteArrayOutputSteam = ByteArrayOutputStream()
        ImageIO.write(bImage, "jpg", byteArrayOutputSteam)
        val data = byteArrayOutputSteam.toByteArray()*/
        var byteArray = message.content.toByteArray()
        attachment?.let {
            byteArray = attachment.bytes
        }

        if (message.properties == null || message.properties?.size == 0) {
            channel.basicPublish("amq.topic", message.topic, null, byteArray)
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