package de.uulm.automotive.cds.models.dtos

import de.uulm.automotive.cds.entities.LocationData
import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.models.FontFamily
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.LocalDateTime

internal class MessageCompactDTOTest {

    private fun createMessageEntity(
            id: Long? = null,
            topic: String? = null,
            sender: String? = null,
            title: String? = null,
            content: String? = null,
            starttime: LocalDateTime? = null,
            endtime: LocalDateTime? = null,
            isSent: Boolean? = null,
            properties: MutableList<String>? = null,
            attachment: ByteArray? = null,
            logoAttachment: ByteArray? = null,
            links: MutableList<URL>? = null,
            locationData: LocationData? = null,
            backgroundColor: String? = null,
            fontColor: String? = null,
            fontFamily: FontFamily? = null
    ): Message = Message(
            id,
            topic,
            sender,
            title,
            content,
            starttime,
            endtime,
            isSent,
            properties,
            attachment,
            logoAttachment,
            links,
            locationData,
            backgroundColor,
            fontColor,
            fontFamily
    )

    private fun createMessageCompactDTO(
            id: Long? = null,
            sender: String? = null,
            title: String? = null,
            content: String? = null,
            starttime: LocalDateTime? = null,
            isSent: Boolean? = null
    ): MessageCompactDTO = MessageCompactDTO(
            id,
            sender,
            title,
            content,
            starttime,
            isSent
    )

    private val defaultDTO : MessageCompactDTO = createMessageCompactDTO(
            7,
            "test sender",
            "test title",
            "test content",
            LocalDateTime.now(),
            false
    )

    private val defaultEntity: Message = createMessageEntity(
            id = defaultDTO.id,
            sender = defaultDTO.sender,
            title = defaultDTO.title,
            content = defaultDTO.content,
            starttime = defaultDTO.starttime,
            isSent = defaultDTO.isSent
    )

    @Test
    fun `message entity to message compact DTO` () {
        val expected = defaultDTO
        val result = MessageCompactDTO.toDTO(defaultEntity)

        assertNotNull(result.id)
        assertNotNull(result.sender)
        assertNotNull(result.title)
        assertNotNull(result.content)
        assertNotNull(result.starttime)
        assertNotNull(result.isSent)

        assertEquals(result.id, expected.id)
        assertEquals(result.sender, expected.sender)
        assertEquals(result.title, expected.title)
        assertEquals(result.content, expected.content)
        assertEquals(result.starttime, expected.starttime)
        assertEquals(result.isSent, expected.isSent)
    }

    @Test
    fun `message compact DTO to message entity` () {
        val expected = defaultEntity
        val result = defaultDTO.toEntity()

        assertNotNull(result.id)
        assertNotNull(result.sender)
        assertNotNull(result.title)
        assertNotNull(result.content)
        assertNotNull(result.starttime)
        assertNotNull(result.isSent)
        assertNull(result.topic)
        assertNull(result.endtime)
        assertNull(result.properties)
        assertNull(result.attachment)
        assertNull(result.logoAttachment)
        assertNull(result.links)
        assertNull(result.locationData)
        assertNull(result.backgroundColor)
        assertNull(result.fontColor)
        assertNull(result.fontFamily)

        assertEquals(result.id, expected.id)
        assertEquals(result.sender, expected.sender)
        assertEquals(result.title, expected.title)
        assertEquals(result.content, expected.content)
        assertEquals(result.starttime, expected.starttime)
        assertEquals(result.isSent, expected.isSent)
    }
}