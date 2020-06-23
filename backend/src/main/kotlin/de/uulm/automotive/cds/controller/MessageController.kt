package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.repositories.MessageRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Controller
@RequestMapping("/message")
/**
 * Controller for Messages.
 */
class MessageController(private val repository: MessageRepository) {

    /**
     * Returns the view for one message.
     *
     * @param id Id of the message
     * @return String name of the view
     */
    @GetMapping("/{id}")
    fun showMessage(@PathVariable id: Long, model: Model): Message {
        model["title"] = "Message"

        val message = repository.findById(id)
        if (message.isEmpty)
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find message with id $id.")

        return message.get()
    }

    /**
     * Saves the given Message.
     *
     * @param message
     * @return String name of the view
     */
    @PostMapping()
    fun saveMessage(@RequestBody message: Message, model: Model) {
        message.isSent = false
        if (message.starttime == null) {
            message.starttime = LocalDateTime.now()
        }
        repository.save(message)
    }
}