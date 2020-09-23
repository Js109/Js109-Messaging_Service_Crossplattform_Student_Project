package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.models.dtos.MessageDTO
import de.uulm.automotive.cds.models.errors.MessageBadRequestInfo
import de.uulm.automotive.cds.repositories.MessageRepository
import de.uulm.automotive.cds.services.MessageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.util.*

@CrossOrigin("*")
@RestController
@RequestMapping("/message")
/**
 * REST-Endpoint for Messages.
 */
class MessageController(private val repository: MessageRepository, private val messageService: MessageService) {

    /**
     * REST-Endpoint to get the message with the specified id in the system.
     * See swagger definition of GET /message/{id} for more details.
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
     * REST-Endpoint for storing a new message.
     * See swagger definition of POST /message for more details.
     *
     * @param messageDto DTO of the message
     * @return Response Entity Containing Error Object in case of an invalid DTO
     */
    @PostMapping
    fun saveMessage(@RequestBody messageDto: MessageDTO): ResponseEntity<MessageBadRequestInfo> {

        val errors = messageDto.getErrors()
        if (errors != null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors)
        }
        val message = messageDto.toEntity()

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

    @DeleteMapping("/{id}")
    fun deleteMessage(@PathVariable id: Long) {
        val message: Optional<Message> = repository.findById(id)

        if (message.isEmpty)
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find message with id $id.")

        if (message.get().isSent!!)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Already sent messages can't be deleted $id.")

        repository.deleteById(id)
    }
}
