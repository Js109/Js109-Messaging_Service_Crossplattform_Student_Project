package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.models.Message
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class MessageController {

    @PostMapping("/message")
    fun relayMessage(@ModelAttribute message: Message) {

    }
}