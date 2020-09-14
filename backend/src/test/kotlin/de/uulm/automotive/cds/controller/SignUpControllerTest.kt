package de.uulm.automotive.cds.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.uulm.automotive.cds.entities.Token
import de.uulm.automotive.cds.models.SignUpInfo
import de.uulm.automotive.cds.models.TokenDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.time.LocalDateTime
import java.util.*


@WebMvcTest
internal class SignUpControllerTest(@Autowired val mockMvc: MockMvc): BaseControllerTest() {

    private val signUpInfo = SignUpInfo("test device Type", UUID.randomUUID())
    private val queueID = UUID.randomUUID()

    private val token = Token(
            signUpInfo.signUpToken,
            queueID,
            LocalDateTime.now().minusMinutes(1),
            null
    )
    private val tokenDTO = TokenDTO.getTokenDTO(token)

    @Test
    fun `new sign up`() {
        every { signUpRepository.save(any<Token>()) } returns token
        every { amqpChannelService.openChannel() } returns mockk()
        every { amqpChannelService.openChannel().queueDeclare(any(), any(), any(), any(), any()) } returns mockk()
        every { amqpChannelService.openChannel().queueBind(any(), any(), any(), any()) } returns mockk()
        every { amqpChannelService.openChannel().close() } returns mockk()
        every { signUpRepository.findBySignUpToken(signUpInfo.signUpToken) } returns null

        mockMvc.post("/signup/") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(signUpInfo)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isOk }
            content { jsonPath("queueID").exists() }
            content { jsonPath("signUpToken").exists() }
        }

        verify(exactly = 1) { signUpRepository.save(any<Token>()) }
    }

    @Test
    fun `try multiple sign ups with the same credentials`() {
        every { signUpRepository.save(any<Token>()) } returns token
        every { amqpChannelService.openChannel() } returns mockk()
        every { amqpChannelService.openChannel().queueDeclare(any(), any(), any(), any(), any()) } returns mockk()
        every { amqpChannelService.openChannel().queueBind(any(), any(), any(), any()) } returns mockk()
        every { amqpChannelService.openChannel().close() } returns mockk()
        every { signUpRepository.findBySignUpToken(signUpInfo.signUpToken) } returns token

        mockMvc.post("/signup/") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(signUpInfo)
            characterEncoding = "UTF-8"
        }.andExpect {
            status { isOk }
            content { jsonPath("queueID").exists() }
            content { jsonPath("signUpToken").exists() }
        }

        verify(exactly = 1) { signUpRepository.save(any<Token>()) }
    }
}