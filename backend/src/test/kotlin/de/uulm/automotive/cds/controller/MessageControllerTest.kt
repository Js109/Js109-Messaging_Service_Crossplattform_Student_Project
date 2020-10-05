package de.uulm.automotive.cds.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.uulm.automotive.cds.entities.LocationData
import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.models.FontFamily
import de.uulm.automotive.cds.models.dtos.MessageDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.net.URL
import java.time.LocalDateTime
import java.util.*

@WebMvcTest
internal class MessageControllerTest(@Autowired val mockMvc: MockMvc) : BaseControllerTest() {

    private val messageBasicAttributesOnly = Message(
            1,
            "test topic",
            "test sender",
            "test title",
            "test content",
            null,
            null,
            false,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    )

    private val emptyMessage = Message(
            5,
            null,
            null,
            null,
            null,
            null,
            null,
            false,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    )

    private fun getMessage(
            id: Long = 1,
            topic: String? = null,
            sender: String = "test sender",
            title: String = "test title",
            content: String = "test content",
            starttime: LocalDateTime? = null,
            endtime: LocalDateTime? = null,
            isSent: Boolean = false,
            properties: MutableList<String> = mutableListOf("test property 1", "test property 2"),
            attachment: ByteArray = ByteArray(150),
            logoAttachment: ByteArray = ByteArray(150),
            links: MutableList<URL> = mutableListOf(URL("https://www.google.com"), URL("https://www.example.com")),
            locationData: LocationData = LocationData(null, 48.3998807, 9.9878078, 10),
            backgroundColor: String = "#f5f5f5",
            fontColor: String = "#F5F5F5",
            fontFamily: FontFamily = FontFamily.ROBOTO
    ): Message {
        return Message(id, topic, sender, title, content, starttime, endtime, isSent, properties,
                attachment, logoAttachment, links, locationData, backgroundColor, fontColor, fontFamily)
    }

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun `show basic Message`() {
        every { messageRepository.findById(1) } returns Optional.of(messageBasicAttributesOnly)

        mockMvc.get("/message/1") {
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("topic").value(messageBasicAttributesOnly.topic!!) }
            content { jsonPath("sender").value(messageBasicAttributesOnly.sender!!) }
            content { jsonPath("title").value(messageBasicAttributesOnly.title!!) }
            content { jsonPath("content").value(messageBasicAttributesOnly.content!!) }
            content { jsonPath("isSent").value(messageBasicAttributesOnly.isSent!!) }
            content { jsonPath("starttime").doesNotExist() }
            content { jsonPath("endtime").doesNotExist() }
            content { jsonPath("properties").doesNotExist() }
            content { jsonPath("attachment").doesNotExist() }
            content { jsonPath("links").doesNotExist() }
            content { jsonPath("locationData").doesNotExist() }
        }
    }

    @Test
    fun `show complete Message`() {
        every { messageRepository.findById(1) } returns Optional.of(getMessage())

        mockMvc.get("/message/1") {
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("topic").doesNotExist() }
            content { jsonPath("sender").value(messageBasicAttributesOnly.sender!!) }
            content { jsonPath("title").value(messageBasicAttributesOnly.title!!) }
            content { jsonPath("content").value(messageBasicAttributesOnly.content!!) }
            content { jsonPath("isSent").value(messageBasicAttributesOnly.isSent!!) }
            content { jsonPath("starttime").doesNotExist() }
            content { jsonPath("endtime").doesNotExist() }
            content { jsonPath("properties").exists() }
            content { jsonPath("attachment").exists() }
            content { jsonPath("links").exists() }
            content { jsonPath("locationData").exists() }
        }
    }

    @Test
    fun `show non existent Message`() {
        every { messageRepository.findById(1) } returns Optional.empty()

        mockMvc.get("/message/1") {
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isNotFound }
        }
    }

    @Test
    fun `save basic Message`() {
        every { messageRepository.save(any<Message>()) } returns messageBasicAttributesOnly
        every { messageService.sendMessage(any()) } returns mockk()

        mockMvc.post("/message") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(messageBasicAttributesOnly)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isOk }
        }

        verify(exactly = 1) { messageRepository.save(any<Message>()) }
    }

    @Test
    fun `save complete Message`() {
        every { messageRepository.save(any<Message>()) } returns messageBasicAttributesOnly
        every { messageService.sendMessage(any()) } returns mockk()

        mockMvc.post("/message") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(MessageDTO.toDTO(getMessage()))
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isOk }
        }

        verify(exactly = 1) { messageRepository.save(any<Message>()) }
    }

    @Test
    fun `save invalid DTO returns Bad Request with BadRequestInfo object in body`() {
        every { messageRepository.save(any<Message>()) } returns messageBasicAttributesOnly
        every { messageService.sendMessage(any()) } returns mockk()

        val invalidDto = MessageDTO(
                locationData = LocationData(null, 0.0, 181.0, 10),
                links = arrayListOf("invalid-link", "http://invalid-link"),
                backgroundColor = "ffffff",
                fontColor = "ffffff"
        )

        mockMvc.post("/message") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(invalidDto)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isUnprocessableEntity }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("senderError").exists() }
            content { jsonPath("senderError").isNotEmpty }
            content { jsonPath("titleError").exists() }
            content { jsonPath("titleError").isNotEmpty }
            content { jsonPath("topicError").exists() }
            content { jsonPath("topicError").isNotEmpty }
            content { jsonPath("contentError").exists() }
            content { jsonPath("contentError").isNotEmpty }
            content { jsonPath("locationError").exists() }
            content { jsonPath("locationError").isNotEmpty }
            content { jsonPath("linkError").exists() }
            content { jsonPath("linkError").isNotEmpty }
            content { jsonPath("backgroundColorError").exists() }
            content { jsonPath("backgroundColorError").isNotEmpty }
            content { jsonPath("fontColorError").exists() }
            content { jsonPath("fontColorError").isNotEmpty }
        }

        verify(exactly = 0) { messageRepository.save(any<Message>()) }
    }

    @Test
    fun `update complete Message`() {
        every { messageRepository.save(any<Message>()) } returns getMessage()
        every { messageRepository.findById(any<Long>()) } returns Optional.of(getMessage()) // returns Optional.of(Empty)

        mockMvc.put("/message/1") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(MessageDTO.toDTO(getMessage()))
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isOk }
        }

        verify(exactly = 1) { messageRepository.save(any<Message>()) }
    }

    @Test
    fun `update Message and Message not saved before`() {
        every { messageRepository.save(any<Message>()) } returns emptyMessage
        every { messageRepository.findById(any<Long>()) } returns Optional.of(emptyMessage)

        mockMvc.put("/message/5") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(MessageDTO.toDTO(emptyMessage))
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isUnprocessableEntity }
        }

        verify(exactly = 0) { messageRepository.save(any<Message>()) }
    }

    @Test
    fun `update invalid DTO returns Bad Request with BadRequestInfo object in body`() {
        every { messageRepository.save(any<Message>()) } returns messageBasicAttributesOnly
        every { messageRepository.findById(any<Long>()) } returns Optional.of(getMessage())

        val invalidDto = MessageDTO(
                locationData = LocationData(null, 0.0, 181.0, 10),
                links = arrayListOf("invalid-link", "http://invalid-link"),
                backgroundColor = "ffffff",
                fontColor = "ffffff"
        )

        mockMvc.put("/message/1") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(invalidDto)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isUnprocessableEntity }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("senderError").exists() }
            content { jsonPath("senderError").isNotEmpty }
            content { jsonPath("titleError").exists() }
            content { jsonPath("titleError").isNotEmpty }
            content { jsonPath("topicError").exists() }
            content { jsonPath("topicError").isNotEmpty }
            content { jsonPath("contentError").exists() }
            content { jsonPath("contentError").isNotEmpty }
            content { jsonPath("locationError").exists() }
            content { jsonPath("locationError").isNotEmpty }
            content { jsonPath("linkError").exists() }
            content { jsonPath("linkError").isNotEmpty }
            content { jsonPath("backgroundColorError").exists() }
            content { jsonPath("backgroundColorError").isNotEmpty }
            content { jsonPath("fontColorError").exists() }
            content { jsonPath("fontColorError").isNotEmpty }
        }

        verify(exactly = 0) { messageRepository.save(any<Message>()) }
    }

    @Test
    fun `delete unsent message deletes message`() {
        every { messageRepository.findById(any()) } returns Optional.of(getMessage(isSent = false))
        every { messageRepository.deleteById(any()) } returns mockk()

        mockMvc.delete("/message/1") {
            accept = MediaType.APPLICATION_JSON
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isOk }
        }

        verify(exactly = 1) { messageRepository.deleteById(any()) }
    }

    @Test
    fun `delete sent message does not delete message and results in BadRequest Response`() {
        every { messageRepository.findById(any()) } returns Optional.of(getMessage(isSent = true))
        every { messageRepository.deleteById(any()) } returns mockk()

        mockMvc.delete("/message/1") {
            accept = MediaType.APPLICATION_JSON
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isBadRequest }
        }

        verify(exactly = 0) { messageRepository.deleteById(any()) }
    }

    @Test
    fun `trying to delete non existing message results in NotFound Response`() {
        every { messageRepository.findById(any()) } returns Optional.empty()
        every { messageRepository.deleteById(any()) } returns mockk()

        mockMvc.delete("/message/1") {
            accept = MediaType.APPLICATION_JSON
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isNotFound }
        }

        verify(exactly = 0) { messageRepository.deleteById(any()) }
    }
}