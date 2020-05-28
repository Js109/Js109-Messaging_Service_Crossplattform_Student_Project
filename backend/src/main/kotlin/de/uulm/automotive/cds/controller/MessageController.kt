package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.models.Message
import de.uulm.automotive.cds.services.AmqpChannelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*

@Controller
class MessageController @Autowired constructor(private val amqpChannelService: AmqpChannelService){

    @GetMapping("/message")
    fun messageForm(model: Model): String {
        model["title"] = "Message"
        return "message"
    }

    @PostMapping("/message")
    fun relayMessage(@ModelAttribute message: Message, model: Model): String {

        val channel = amqpChannelService.openChannel()

        channel.queueDeclare(message.topic, true, false, false, null)
        channel.basicPublish("", message.topic, null, message.content.toByteArray())

        model["topic"] = message.topic
        model["content"] = message.content

        channel.close()

        return "sentMessage"
    }
}