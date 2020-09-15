package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.models.MessageBadRequestInfo
import de.uulm.automotive.cds.repositories.MessageRepository
import de.uulm.automotive.cds.services.MessageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    fun saveMessage(@RequestBody message: Message): ResponseEntity<MessageBadRequestInfo> {

        val (hasErrors, errors) = validateMessage(message)
        if (hasErrors) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors)
        }

        if (message.starttime == null) {
            message.starttime = LocalDateTime.now()
            message.isSent = true
            messageService.sendMessage(message)
        } else {
            message.isSent = false
        }
        repository.save(message)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    private fun validateMessage(message: Message): Pair<Boolean, MessageBadRequestInfo> {
        var hasErrors = false
        val errors = MessageBadRequestInfo()

        //double negative is used for empty checks to also include null checks, as message.field.isEmpty() == true would not be true for null
        if (message.sender?.isNotEmpty() != true) {
            errors.senderError = "Sender field is required."
            hasErrors = true
        }

        if (message.title?.isNotEmpty() != true) {
            errors.titleError = "Title field is required."
            hasErrors = true
        }

        if (message.topic?.isNotEmpty() != true && message.properties?.isNotEmpty() != true) {
            errors.topicError = "Either Topics or Properties are required."
            hasErrors = true
        }

        if (message.content?.isNotEmpty() != true && message.attachment?.isNotEmpty() != true) {
            errors.contentError = "Either Content or Files are required."
            hasErrors = true
        }

        message.locationData?.let {
            if (it.lat < -90 || it.lat > 90 || it.lng > 180 || it.lng < -180) {
                hasErrors = true
                errors.locationError = "Please check your coordinate values!"
            }
        }

        return Pair(hasErrors, errors)
    }
}
