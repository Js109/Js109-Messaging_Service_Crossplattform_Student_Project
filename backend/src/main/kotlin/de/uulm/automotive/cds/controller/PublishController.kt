package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.repositories.MessageRepository
import de.uulm.automotive.cds.repositories.PropertyRepository
import de.uulm.automotive.cds.repositories.TopicRepository
import de.uulm.automotive.cds.services.MessageService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

/**
 * Controller for the OEM web view.
 */
@Controller
@RequestMapping("/publish")
class PublishController(private val messageRepository: MessageRepository, private val topicRepository: TopicRepository, private val propertyRepository: PropertyRepository, private val messageService: MessageService) {

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
            properties,
            id
    )

    /**
     * Post resource for the form in the create-message view.
     * Returns a view showing the details of the stored message.
     *
     * @param message Message generated from the request params.
     * @param messagestarttime LocalDateTime for the starttime of the message that is passed separately to specifically set the necessary format.
     * @return String name of the view
     */
    @PostMapping()
    fun postMessage(message: Message, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) messagestarttime: LocalDateTime?, model: Model): String {
        message.isSent = false
        if (messagestarttime == null) {
            message.starttime = LocalDateTime.now()
            message.isSent = true
            messageService.sendMessage(message)
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
            val properties: MutableList<String>?,
            val id: Long?
    )

}