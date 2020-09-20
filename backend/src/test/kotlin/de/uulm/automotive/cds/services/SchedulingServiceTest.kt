package de.uulm.automotive.cds.services

import de.uulm.automotive.cds.entities.LocationData
import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.entities.Token
import de.uulm.automotive.cds.repositories.MessageRepository
import de.uulm.automotive.cds.repositories.SignUpRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.LocalDateTime
import java.util.*

internal class SchedulingServiceTest {

    private val messageRepository: MessageRepository = mockk()
    private val messageService: MessageService = mockk()
    private val tokenRepository: SignUpRepository = mockk()
    private val schedulingService: SchedulingService = SchedulingService(messageRepository, tokenRepository, messageService, 100)

    private val message = Message(
            1,
            "test topic",
            "test sender",
            "test title",
            "test content",
            LocalDateTime.now().minusHours(1),
            LocalDateTime.now().plusHours(2),
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

    private val message2 = Message(
            2,
            null,
            "test sender",
            "test title",
            "test content",
            LocalDateTime.now().minusHours(1),
            LocalDateTime.now().plusHours(1),
            false,
            mutableListOf("test property 1", "test property 2"),
            ByteArray(150),
            ByteArray(150),
            mutableListOf(URL("https://www.google.com"), URL("https://www.example.com")),
            LocationData(null, 48.3998807, 9.9878078, 10),
            null,
            null,
            null
    )

    private val messages = listOf<Message>(message, message2)

    private val token = Token(
            UUID.randomUUID(),
            UUID.randomUUID(),
            LocalDateTime.now().minusMinutes(1),
            null
    )
    private val tokens = listOf<Token>(
            Token(
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    LocalDateTime.now().minusDays(1),
                    null
            ),
            Token(
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    LocalDateTime.now(),
                    null
            )
    )

    @Test
    fun `send messages`() {

        every { messageService.filterCurrentMessages() } returns messages
        every { messageService.sendMessage(any()) } returns Unit
        every { messageRepository.save(any<Message>()) } returns message

        schedulingService.sendMessages()

        verify(exactly = 2) { messageRepository.save(any<Message>()) }
    }

    @Test
    fun `remove old sign up tokens`() {
        every { tokenRepository.findAll() } returns tokens
        every { tokenRepository.delete(any()) } returns mockk()

        schedulingService.removeOldSignUpTokens()

        verify(exactly = 1) { tokenRepository.delete(any()) }
    }
}