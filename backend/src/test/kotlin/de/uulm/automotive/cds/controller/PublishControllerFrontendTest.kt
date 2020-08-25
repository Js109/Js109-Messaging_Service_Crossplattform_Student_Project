package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.entities.Message
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class PublishControllerFrontendTest(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun messageForm() {
        val entity = restTemplate.getForEntity<String>("/publish")
        assertEquals(entity.statusCode, HttpStatus.OK)
        assertThat(entity.body).contains("<h3>Message</h3>")

    }

    @Test
    fun showMessages() {
        val entity = restTemplate.getForEntity<String>("/publish/messages")
        assertEquals(entity.statusCode, HttpStatus.OK)
        assertThat(entity.body).contains("<h3>Messages</h3>")
    }
}