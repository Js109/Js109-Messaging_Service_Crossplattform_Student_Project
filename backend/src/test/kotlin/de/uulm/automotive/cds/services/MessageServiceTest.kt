package de.uulm.automotive.cds.services

import de.uulm.automotive.cds.entities.LocationData
import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.repositories.MessageRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.LocalDateTime

internal class MessageServiceTest {

    private val messageRepository: MessageRepository = mockk()
    private val amqpChannelService: AmqpChannelService = mockk()
    private val messageService: MessageService = MessageService(amqpChannelService, messageRepository)

    private val message = Message(
            1,
            "test topic",
            "test sender",
            "test title",
            "test content",
            LocalDateTime.now().plusHours(1),
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

    @Test
    fun `send messages`() {
        every { amqpChannelService.openChannel().basicPublish(any(), any(), any(), any()) } returns mockk()
        every { amqpChannelService.openChannel().close() } returns mockk()

        messageService.sendMessage(message2)
        messageService.sendMessage(message)

        verify(exactly = 2) { amqpChannelService.openChannel().basicPublish(any(), any(), any(), any()) }
    }

    @Test
    fun `filter current messages`() {
        every { messageRepository.findAllByIsSentFalseOrderByStarttimeAsc() } returns messages

        val currentMessages = messageService.filterCurrentMessages()

        assertEquals(currentMessages.size, 1)
        assertTrue(currentMessages.contains(message2))
    }
}