package de.uulm.automotive.cds.controller

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.repositories.MessageRepository
import de.uulm.automotive.cds.repositories.PropertyRepository
import de.uulm.automotive.cds.repositories.TopicRepository
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Controller
@RequestMapping("/publish")
class PublishController(private val messageRepository: MessageRepository, private val topicRepository: TopicRepository, private val propertyRepository: PropertyRepository) {

    /**
     * Returns a view to create a new message.
     *
     * @return String name of the view
     */
    @GetMapping()
    fun messageForm(model: Model): String {
        model["title"] = "Message"
        model["topics"] = topicRepository.findAllByOrderByTitleAsc()
        model["properties"] = propertyRepository.findAllByOrderByNameAsc()
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
        model["messages"] = messageRepository.findAllByOrderByTopicAsc().map { it.render() }
        return "messages"
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
     * Saves the given Message.
     *
     * @param message
     * @return String name of the view
     */
    @PostMapping()
    fun saveMessage(message: Message, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) messagestarttime: LocalDateTime?, model: Model): String {
        message.isSent = false
        if (messagestarttime == null) {
            message.starttime = LocalDateTime.now()
        } else {
            message.starttime = messagestarttime
        }
        val savedMessage = messageRepository.save(message)
        model["title"] = "Messages"
        model["message"] = savedMessage.render()
        return "message"
    }

    /**
     * Rendered Version of the Message Class.
     */
    data class RenderedMessage(
            val topic: String?,
            val content: String,
            val starttime: LocalDateTime?,
            val endtime: LocalDateTime?,
            val isSent: Boolean?,
            val id: Long?
    )

}