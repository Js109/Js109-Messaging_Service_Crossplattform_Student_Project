package de.uulm.automotive.cds.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.uulm.automotive.cds.entities.TemplateMessage
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.core.IsNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@WebMvcTest
internal class TemplateControllerTest(@Autowired val mockMvc: MockMvc) : BaseControllerTest() {

    val template = TemplateMessage(
            null,
            "Template 1",
            "test topic",
            null,
            "test title",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    )
    val template2 = TemplateMessage(
            null,
            "Template 2",
            null,
            "test sender",
            null,
            "test content",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    )

    @Test
    fun `get all templates`() {
        every { templateRepository.findAllByOrderByTemplateNameAsc() } returns listOf(template, template2)

        mockMvc.get("/template") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { jsonPath("\$.[0].templateName").value("Template 1") }
            content { jsonPath("\$.[0].topic").value("test topic") }
            content { jsonPath("\$.[0].sender").value(IsNull.nullValue()) }
            content { jsonPath("\$.[1].templateName").value("Template 2") }
            content { jsonPath("\$.[1].topic").value(IsNull.nullValue()) }
            content { jsonPath("\$.[1].sender").value("test sender") }
        }
    }

    @Test
    fun `save TemplateMessage`() {
        every { templateRepository.save(any<TemplateMessage>()) } returns template

        mockMvc.post("/template") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(template)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isOk }
        }
        verify(exactly = 1) { templateRepository.save(any<TemplateMessage>()) }
    }

    @Test
    fun `delete template`() {
        every { templateRepository.deleteById(any()) } returns Unit

        mockMvc.delete("/template/11") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
        }
        val captureSlot = slot<Long>()
        verify(exactly = 1) { templateRepository.deleteById(capture(captureSlot)) }
        assertThat(captureSlot.captured).isEqualTo(11)
    }

}