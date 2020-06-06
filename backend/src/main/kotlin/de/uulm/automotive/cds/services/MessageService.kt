package de.uulm.automotive.cds.services

import com.rabbitmq.client.ConnectionFactory
import de.uulm.automotive.cds.Message

class MessageService {

    fun sendMessage(message: Message) {
        val factory = ConnectionFactory()
        factory.host = "134.60.157.15"
        factory.username = "android_cl"
        factory.password = "supersecure"

        val connection = factory.newConnection()
        val channel = connection.createChannel()

        channel.queueDeclare(message.topic, true, false, false, null)
        channel.basicPublish("", message.topic, null, message.content.toByteArray())
    }
}