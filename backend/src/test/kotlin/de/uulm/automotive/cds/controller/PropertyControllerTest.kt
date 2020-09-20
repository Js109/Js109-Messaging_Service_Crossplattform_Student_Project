package de.uulm.automotive.cds.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.uulm.automotive.cds.entities.Property
import de.uulm.automotive.cds.models.dtos.PropertyDTO
import io.mockk.every
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


@WebMvcTest
internal class PropertyControllerTest(@Autowired val mockMvc: MockMvc): BaseControllerTest() {

    private val property = PropertyDTO()
    private val property2 = PropertyDTO()

    init {
        property.name = "test property"
        property.binding = "test binding"

        property2.name = "test property 2"
        property2.binding = "test binding 2"

    }

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun `get all properties`() {
        every { propertyRepository.findAll() } returns listOf(property.toEntity(), property2.toEntity())

        mockMvc.get("/property") {
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("\$.[0].name").value(property.name) }
            content { jsonPath("\$.[0].binding").value(property.binding) }
            content { jsonPath("\$.[1].name").value(property2.name) }
            content { jsonPath("\$.[1].binding").value(property2.binding) }
        }
    }

    @Test
    fun `save Property`() {
        every { propertyRepository.save(any<Property>()) } returns property.toEntity()

        mockMvc.post("/property") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(property)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isOk }
        }

        verify(exactly = 1) { propertyRepository.save(any<Property>()) }
    }

    @Test
    fun `save invalid DTO returns Bad Request with BadRequestInfo object in body`() {
        val invalidDto = PropertyDTO(
                "",
                "   "
        )

        mockMvc.post("/property") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(invalidDto)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isBadRequest }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("nameError").exists() }
            content { jsonPath("nameError").isNotEmpty }
            content { jsonPath("bindingError").exists() }
            content { jsonPath("bindingError").isNotEmpty }
        }

        verify(exactly = 0) { propertyRepository.save(any<Property>()) }
    }
}