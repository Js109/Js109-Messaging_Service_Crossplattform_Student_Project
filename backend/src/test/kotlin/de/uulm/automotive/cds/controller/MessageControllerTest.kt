package de.uulm.automotive.cds.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import de.uulm.automotive.cds.entities.LocationData
import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.repositories.MessageRepository
import de.uulm.automotive.cds.repositories.PropertyRepository
import de.uulm.automotive.cds.repositories.SignUpRepository
import de.uulm.automotive.cds.repositories.TopicRepository
import de.uulm.automotive.cds.services.AmqpChannelService
import de.uulm.automotive.cds.services.MessageService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.net.URL
import java.util.*

@WebMvcTest
internal class MessageControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    private lateinit var messageRepository: MessageRepository

    @MockkBean
    private lateinit var propertyRepository: PropertyRepository

    @MockkBean
    private lateinit var topicRepository: TopicRepository

    @MockkBean
    private lateinit var signUpRepository: SignUpRepository

    @MockkBean
    private lateinit var messageService: MessageService

    @MockkBean
    private lateinit var amqpChannelService: AmqpChannelService

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
            null
    )

    private val message = Message(
            1,
            "test topic",
            "test sender",
            "test title",
            "test content",
            null,
            null,
            false,
            mutableListOf("test property 1", "test property 2"),
            ByteArray(150),
            mutableListOf(URL("https://www.google.com"), URL("https://www.example.com")),
            LocationData(null, 48.3998807, 9.9878078, 10)
    )

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
        every { messageRepository.findById(1) } returns Optional.of(message)

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
            content = jacksonObjectMapper().writeValueAsString(message)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isOk }
        }

        verify(exactly = 1) { messageRepository.save(any<Message>()) }
    }

    @Test
    fun `missing sender creates 404`() {
        every { messageRepository.save(any<Message>()) } returns messageBasicAttributesOnly
        every { messageService.sendMessage(any()) } returns mockk()

        val errorMessage = Message(
                1,
                "test topic",
                null,
                "test title",
                "test content",
                null,
                null,
                false,
                null,
                null,
                null,
                null
        )

        mockMvc.post("/message") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(errorMessage)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isBadRequest }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("senderError").exists() }
            content { jsonPath("senderError").isNotEmpty }
            content { jsonPath("topicError").doesNotExist() }
            content { jsonPath("titleError").doesNotExist() }
            content { jsonPath("contentError").doesNotExist() }
            content { jsonPath("locationError").doesNotExist() }
        }
    }

    @Test
    fun `missing title creates 404`() {
        every { messageRepository.save(any<Message>()) } returns messageBasicAttributesOnly
        every { messageService.sendMessage(any()) } returns mockk()

        val errorMessage = Message(
                1,
                "test topic",
                "test sender",
                null,
                "test content",
                null,
                null,
                false,
                null,
                null,
                null,
                null
        )

        mockMvc.post("/message") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(errorMessage)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isBadRequest }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("titleError").exists() }
            content { jsonPath("titleError").isNotEmpty }
            content { jsonPath("topicError").doesNotExist() }
            content { jsonPath("senderError").doesNotExist() }
            content { jsonPath("contentError").doesNotExist() }
            content { jsonPath("locationError").doesNotExist() }
        }
    }

    @Test
    fun `missing topic or properties creates 404`() {
        every { messageRepository.save(any<Message>()) } returns messageBasicAttributesOnly
        every { messageService.sendMessage(any()) } returns mockk()

        val errorMessage = Message(
                1,
                null,
                "test sender",
                "test title",
                "test content",
                null,
                null,
                false,
                null,
                null,
                null,
                null
        )

        mockMvc.post("/message") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(errorMessage)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isBadRequest }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("topicError").exists() }
            content { jsonPath("topicError").isNotEmpty }
            content { jsonPath("titleError").doesNotExist() }
            content { jsonPath("senderError").doesNotExist() }
            content { jsonPath("contentError").doesNotExist() }
            content { jsonPath("locationError").doesNotExist() }
        }
    }

    @Test
    fun `only properties and no topic creates 200`() {
        every { messageRepository.save(any<Message>()) } returns messageBasicAttributesOnly
        every { messageService.sendMessage(any()) } returns mockk()

        val errorMessage = Message(
                1,
                null,
                "test sender",
                "test title",
                "test content",
                null,
                null,
                null,
                mutableListOf("test property"),
                null,
                null,
                null
        )

        mockMvc.post("/message") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(errorMessage)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isOk }
        }
    }

    @Test
    fun `missing content or attachment creates 404`() {
        every { messageRepository.save(any<Message>()) } returns messageBasicAttributesOnly
        every { messageService.sendMessage(any()) } returns mockk()

        val errorMessage = Message(
                1,
                "test topic",
                "test sender",
                "test title",
                null,
                null,
                null,
                false,
                null,
                null,
                null,
                null
        )

        mockMvc.post("/message") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(errorMessage)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isBadRequest }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("contentError").exists() }
            content { jsonPath("contentError").isNotEmpty }
            content { jsonPath("titleError").doesNotExist() }
            content { jsonPath("senderError").doesNotExist() }
            content { jsonPath("topicError").doesNotExist() }
            content { jsonPath("locationError").doesNotExist() }
        }
    }

    @Test
    fun `only attachment and no content creates 200`() {
        every { messageRepository.save(any<Message>()) } returns messageBasicAttributesOnly
        every { messageService.sendMessage(any()) } returns mockk()

        val errorMessage = Message(
                1,
                "test topic",
                "test sender",
                "test title",
                null,
                null,
                null,
                null,
                null,
                ByteArray(10) { pos -> pos.toByte() },
                null,
                null
        )

        mockMvc.post("/message") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(errorMessage)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isOk }
        }
    }

    @Test
    fun `longitude out of valid range creates 404`() {
        every { messageRepository.save(any<Message>()) } returns messageBasicAttributesOnly
        every { messageService.sendMessage(any()) } returns mockk()

        val errorMessage = Message(
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
                LocationData(null, 0.0, 181.0, 10)
        )

        mockMvc.post("/message") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(errorMessage)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isBadRequest }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("locationError").exists() }
            content { jsonPath("locationError").isNotEmpty }
            content { jsonPath("titleError").doesNotExist() }
            content { jsonPath("senderError").doesNotExist() }
            content { jsonPath("topicError").doesNotExist() }
            content { jsonPath("contentError").doesNotExist() }
        }
    }

    @Test
    fun `latitude out of valid range creates 404`() {
        every { messageRepository.save(any<Message>()) } returns messageBasicAttributesOnly
        every { messageService.sendMessage(any()) } returns mockk()

        val errorMessage = Message(
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
                LocationData(null, 91.0, 0.0, 10)
        )

        mockMvc.post("/message") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(errorMessage)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isBadRequest }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("locationError").exists() }
            content { jsonPath("locationError").isNotEmpty }
            content { jsonPath("titleError").doesNotExist() }
            content { jsonPath("senderError").doesNotExist() }
            content { jsonPath("topicError").doesNotExist() }
            content { jsonPath("contentError").doesNotExist() }
        }
    }

    @Test
    fun `latitude and longitude invalid range creates 200`() {
        every { messageRepository.save(any<Message>()) } returns messageBasicAttributesOnly
        every { messageService.sendMessage(any()) } returns mockk()

        val errorMessage = Message(
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
                LocationData(null, 90.0, 180.0, 10)
        )

        mockMvc.post("/message") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(errorMessage)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isOk }
        }
    }
}