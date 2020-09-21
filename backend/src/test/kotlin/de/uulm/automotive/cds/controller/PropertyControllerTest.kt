package de.uulm.automotive.cds.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.uulm.automotive.cds.entities.Property
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

    private val property = Property()
    private val property2 = Property()

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
        every { propertyRepository.findAll() } returns listOf(property, property2)

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
        every { propertyRepository.save(any<Property>()) } returns property

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
}