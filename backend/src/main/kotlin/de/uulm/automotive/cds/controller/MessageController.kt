package de.uulm.automotive.cds.controller

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
    fun relayMessage(@ModelAttribute message: Message) {

    }
}