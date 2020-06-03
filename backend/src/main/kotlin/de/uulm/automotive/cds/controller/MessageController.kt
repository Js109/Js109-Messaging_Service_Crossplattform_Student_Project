package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.Message
import de.uulm.automotive.cds.MessageRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import java.time.LocalDateTime

@Controller
class MessageController(private val repository: MessageRepository) {

    @GetMapping("/message")
    fun messageForm(model: Model): String {
        model["title"] = "Message"
        return "create-message"
    }

    @GetMapping("/messages")
    fun showMessages(model: Model): String {
        model["title"] = "Messages"
        model["messages"] = repository.findAllByOrderByTopicAsc().map { it.render() }
        return "messages"
    }

    @GetMapping("/message/{id}")
    fun showMessage(@PathVariable id: Long, model: Model): String {
        model["title"] = "Message"

        val message = repository.findById(id)
        if (message.isEmpty)
            return showMessages(model)

        model["message"] = message.get().render()

        return "message"
    }

    @PostMapping("/message")
    fun saveMessage(@ModelAttribute message: Message, model: Model): String {
        message.isSent = false
        if(message.starttime == null) {
            message.starttime = LocalDateTime.now()
        }

        val savedMessage = repository.save(message)

        model["title"] = "Messages"
        model["message"] = savedMessage.render()

        return "message"
    }

    fun Message.render() = RenderedMessage(
            topic,
            content,
            starttime,
            endtime,
            isSent,
            id
    )

    data class RenderedMessage(
            val topic: String,
            val content: String,
            val starttime: LocalDateTime?,
            val endtime: LocalDateTime?,
            val isSent: Boolean?,
            val id: Long?
    )
}