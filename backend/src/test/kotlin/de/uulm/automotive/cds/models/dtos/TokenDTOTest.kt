package de.uulm.automotive.cds.models.dtos

import de.uulm.automotive.cds.entities.Token
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

internal class TokenDTOTest {

    private val token: Token = Token(
            UUID.randomUUID(),
            UUID.randomUUID(),
            LocalDateTime.now(),
            null
    )

    private fun getTokenDto(): TokenDTO {
        return TokenDTO.toDTO(token)
    }

    @Test
    fun `token entity to token dto`() {
        val expected = token
        val result = getTokenDto()

        assertEquals(expected.signUpToken, result.signUpToken)
        assertEquals(expected.queueId, result.queueID)
    }

}