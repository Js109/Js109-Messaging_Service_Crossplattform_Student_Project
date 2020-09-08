package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.repositories.MessageRepository
import de.uulm.automotive.cds.services.MessageService
import org.springframework.http.HttpStatus
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@CrossOrigin("*")
@RestController
@RequestMapping("/message")
/**
 * Controller for Messages.
 */
class MessageController(private val repository: MessageRepository, private val messageService: MessageService) {

    /**
     * Returns the view for one message.
     *
     * @param id Id of the message
     * @return Message with the specified id
     */
    @GetMapping("/{id}")
    fun showMessage(@PathVariable id: Long): Message {

        val message = repository.findById(id)
        if (message.isEmpty)
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find message with id $id.")

        return message.get()
    }

    /**
     * Saves the given Message.
     *
     * @param message
     */
    @PostMapping
    fun saveMessage(@RequestBody message: Message) {
        if (message.starttime == null) {
            message.starttime = LocalDateTime.now()
            message.isSent = true
            messageService.sendMessage(message)
        } else {
            message.isSent = false
        }
        repository.save(message)
    }
}
