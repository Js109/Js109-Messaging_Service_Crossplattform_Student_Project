package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.repositories.MessageRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@Controller
/**
 * Controller for Messages.
 */
class MessageController(private val repository: MessageRepository) {

    /**
     * Returns a view to create a new message.
     *
     * @return String name of the view
     */
    @GetMapping("/message")
    fun messageForm(model: Model): String {
        model["title"] = "Message"
        return "create-message"
    }

    /**
     * Returns a view containing all messages.
     *
     * @return String name of the view
     */
    @GetMapping("/messages")
    fun showMessages(model: Model): String {
        model["title"] = "Messages"
        model["messages"] = repository.findAllByOrderByTopicAsc().map { it.render() }
        return "messages"
    }

    /**
     * Returns the view for one message.
     *
     * @param id Id of the message
     * @return String name of the view
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
     * Saves the given Message.
     *
     * @param message
     * @return String name of the view
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
     * Removes Unused values from the Message class that should not be available to the Client.
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
     * Rendered Version of the Message Class.
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