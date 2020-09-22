package de.uulm.automotive.cds.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.uulm.automotive.cds.entities.Topic
import de.uulm.automotive.cds.models.dtos.TopicDTO
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@WebMvcTest
internal class TopicControllerTest(@Autowired val mockMvc: MockMvc): BaseControllerTest() {

    private val topic = TopicDTO()
    private val topic2 = TopicDTO()

    init {
        topic.title = "test title"
        topic.binding = "test binding"
        topic.description = "test description"
        topic.tags = arrayListOf("test tag 1", "test tag 2")

        topic2.title = "test title 2"
        topic2.binding = "test binding 2"
        topic2.description = "test description 2"
        topic2.tags = arrayListOf("test tag 3", "test tag 2")
    }

    @Test
    fun `get all Topics`() {
        every { topicRepository.findAll() } returns listOf(topic.toEntity(), topic2.toEntity())

        mockMvc.get("/topic") {
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("\$.[0].title").value(topic.title) }
            content { jsonPath("\$.[0].binding").value(topic.binding) }
            content { jsonPath("\$.[0].description").value(topic.description) }
            content { jsonPath("\$.[0].tags").value(topic.tags) }
            content { jsonPath("\$.[1].title").value(topic2.title) }
            content { jsonPath("\$.[1].binding").value(topic2.binding) }
            content { jsonPath("\$.[1].description").value(topic2.description) }
            content { jsonPath("\$.[1].tags").value(topic2.tags) }
        }
    }

    @Test
    fun `save Topic`() {
        every { topicRepository.findByBinding(any()) } returns topic.toEntity()

        mockMvc.post("/topic") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(topic)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isUnprocessableEntity }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("bindingError").exists() }
            content { jsonPath("bindingError").isNotEmpty }
            content { jsonPath("titleError").doesNotExist() }
            content { jsonPath("descriptionError").doesNotExist() }

        }

        verify(exactly = 0) { topicRepository.save(any<Topic>()) }
    }

    @Test
    fun `save invalid DTO returns Bad Request with BadRequestInfo object in body`() {
        val invalidDto = TopicDTO(
                "",
                "",
                arrayListOf(),
                "    "
        )

        mockMvc.post("/topic") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(invalidDto)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isUnprocessableEntity }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("bindingError").exists() }
            content { jsonPath("bindingError").isNotEmpty }
            content { jsonPath("titleError").exists() }
            content { jsonPath("titleError").isNotEmpty }
            content { jsonPath("descriptionError").exists() }
            content { jsonPath("descriptionError").isNotEmpty }

        }

        verify(exactly = 0) { topicRepository.save(any<Topic>()) }
    }
}