package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.Message
import de.uulm.automotive.cds.MessageRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@Controller
class MessageController(private val repository: MessageRepository) {

    /**
     * TODO
     *
     * @param model
     * @return String
     */
    @GetMapping("/message")
    fun messageForm(model: Model): String {
        model["title"] = "Message"
        return "create-message"
    }

    /**
     * TODO
     *
     * @param model
     * @return String
     */
    @GetMapping("/messages")
    fun showMessages(model: Model): String {
        model["title"] = "Messages"
        model["messages"] = repository.findAllByOrderByTopicAsc().map { it.render() }
        return "messages"
    }

    /**
     * TODO
     *
     * @param id
     * @param model
     * @return String
     */
    @GetMapping("/message/{id}")
    fun showMessage(@PathVariable id: Long, model: Model): String {
        model["title"] = "Message"

        val message = repository.findById(id)
        if (message.isEmpty)
            return showMessages(model)

        model["message"] = message.get().render()
        return "message"
    }

    /**
     * TODO
     *
     * @param message
     * @param model
     * @return String
     */
    @PostMapping("/message")
    fun saveMessage(@RequestBody message: Message, model: Model): String {
        message.isSent = false
        if (message.starttime == null) {
            message.starttime = LocalDateTime.now()
        }
        val savedMessage = repository.save(message)
        model["title"] = "Messages"
        model["message"] = savedMessage.render()
        return "message"
    }

    /**
     * TODO
     *
     */
    fun Message.render() = RenderedMessage(
        topic,
        content,
        starttime,
        endtime,
        isSent,
        id
    )

    /**
     * TODO
     *
     * @property topic
     * @property content
     * @property starttime
     * @property endtime
     * @property isSent
     * @property id
     */
    data class RenderedMessage(
        val topic: String,
        val content: String,
        val starttime: LocalDateTime?,
        val endtime: LocalDateTime?,
        val isSent: Boolean?,
        val id: Long?
    )
}