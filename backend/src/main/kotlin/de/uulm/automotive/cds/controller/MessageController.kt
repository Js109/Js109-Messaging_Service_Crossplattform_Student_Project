package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.models.dtos.MessageCompactDTO
import de.uulm.automotive.cds.models.dtos.MessageDTO
import de.uulm.automotive.cds.models.errors.MessageBadRequestInfo
import de.uulm.automotive.cds.repositories.MessageRepository
import de.uulm.automotive.cds.services.MessageService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import javax.transaction.Transactional

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
     * Returns the view for all messages in a period, a partial sequence search and for a specific topic.
     *
     * @param searchString A string the user is searching for as partial sequences
     * @param startTimePeriod the date which is the start date for the period of the messages to be displayed
     * @param endTimePeriod the date which is the end date for the period of the messages to be displayed
     * @param topic A string which contains the topic name of the messages to be displayed
     * @return all messages in a period, a partial sequence search and for a specific topic
     */
    @GetMapping
    @Transactional
    fun showMessages(@RequestParam searchString: String? = null,
                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startTimePeriod: LocalDate? = null,
                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endTimePeriod: LocalDate? = null,
                     @RequestParam topic: String? = null,
                     @RequestParam property: String? = null, @RequestParam sender: String? = null,
                     @RequestParam content: String? = null, @RequestParam title: String? = null): Iterable<MessageCompactDTO> =
            messageService.filterMessages(
                    topicName = topic,
                    searchString = searchString,
                    timeSpanBegin = startTimePeriod?.let {
                        LocalDateTime.of(it, LocalTime.MIN)
                    },
                    timeSpanEnd = endTimePeriod?.let {
                        LocalDateTime.of(it, LocalTime.MAX)
                    },
                    sender = sender,
                    content = content,
                    title = title
            )

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
        val message = MessageDTO.toEntity(messageDto)

        if (message.starttime == null || message.starttime?.isBefore(LocalDateTime.now()) != false) {
            message.starttime = LocalDateTime.now()
            message.isSent = true
            messageService.sendMessage(message)
        } else {
            message.isSent = false
        }
        repository.save(message)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    /**
     * REST-Endpoint for deleting a message.
     * See swagger definition of DELETE /message for more details.
     *
     * @param id id of the message
     */
    @DeleteMapping("/{id}")
    fun deleteMessage(@PathVariable id: Long) {
        val message: Optional<Message> = repository.findById(id)

        if (message.isEmpty)
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find message with id $id.")

        if (message.get().isSent!!)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Already sent messages can't be deleted $id.")

        repository.deleteById(id)
    }

    /**
     * REST-Endpoint for updating a message.
     * See swagger definition of PUT /message for more details.
     *
     * @param id id of the message
     * @param messageDto DTO of the message
     * @return Response Entity Containing Error Object in case of an invalid DTO
     */
    @PutMapping("/{id}")
    fun updateMessage(@PathVariable id: Long, @RequestBody messageDto: MessageDTO): ResponseEntity<MessageBadRequestInfo> {
        val messageOld = repository.findById(id)
        if (messageOld.isEmpty)
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find message with id $id and thus cannot update it.")

        val errors = messageDto.getErrors()
        if (errors != null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors)
        }

        val message = MessageDTO.toEntity(messageDto)
        message.id = id
        message.isSent = false

        return if (messageOld.get().isSent == false) {
            repository.save(message)
            ResponseEntity.status(HttpStatus.OK).build()
        } else ResponseEntity.status(HttpStatus.LOCKED).build()
    }
}
