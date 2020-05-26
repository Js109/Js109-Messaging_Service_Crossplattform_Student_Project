package de.uulm.automotive.cds.controller

import com.rabbitmq.client.ConnectionFactory
import de.uulm.automotive.cds.models.Message
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*

@Controller
class MessageController {

    @GetMapping("/message")
    fun messageForm(model: Model): String {
        model["title"] = "Message"
        return "message"
    }

    @PostMapping("/message")
    fun relayMessage(@ModelAttribute message: Message, model: Model): String {
        val factory = ConnectionFactory()
        factory.host = "134.60.157.15"
        factory.username = "android_cl"
        factory.password = "supersecure"

        val connection = factory.newConnection()
        val channel = connection.createChannel()

        channel.queueDeclare(message.topic, true, false, false, null)
        channel.basicPublish("", message.topic, null, message.content.toByteArray())

        model["topic"] = message.topic
        model["content"] = message.content

        return "sentMessage"
    }
}