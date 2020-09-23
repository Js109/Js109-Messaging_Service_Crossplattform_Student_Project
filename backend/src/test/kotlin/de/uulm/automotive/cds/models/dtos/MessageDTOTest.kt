package de.uulm.automotive.cds.models.dtos

import de.uulm.automotive.cds.entities.LocationData
import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.models.FontFamily
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.LocalDateTime

internal class MessageDTOTest {

    private val message: Message = Message(
            2,
            "topic",
            "sender",
            "title",
            "content",
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(1),
            false,
            arrayListOf("property1", "property2"),
            ByteArray(150),
            ByteArray(10),
            arrayListOf(URL("https://www.example.com"), URL("https://example2.com")),
            LocationData(1, 10.0, 10.0, 10),
            "#f5f5f5",
            "#F5F5F5",
            FontFamily.ARIAL
    )

    private fun getMessageEntity(): Message {
        return message
    }

    private fun getMessageDTO(
            updatedTopic: String? = null,
            updatedSender: String? = null,
            updatedTitle: String? = null,
            updatedContent: String? = null,
            updatedStarttime: LocalDateTime? = null,
            updatedEndtime: LocalDateTime? = null,
            updatedProperties: MutableList<String>? = null,
            updatedAttachment: ByteArray? = null,
            updatedLogoAttachment: ByteArray? = null,
            updatedLinks: MutableList<String>? = arrayListOf("https://www.example.com"),
            updatedLocationData: LocationData? = null,
            updatedBackgroundColor: String? = null,
            updatedFontColor: String? = null,
            updatedFontFamily: FontFamily? = null
    ): MessageDTO {
        return MessageDTO(
                updatedTopic ?: message.topic,
                updatedSender ?: message.sender,
                updatedTitle ?: message.title,
                updatedContent ?: message.content,
                updatedStarttime ?: message.starttime,
                updatedEndtime ?: message.endtime,
                updatedProperties ?: message.properties,
                updatedAttachment ?: message.attachment,
                updatedLogoAttachment ?: message.logoAttachment,
                updatedLinks,
                updatedLocationData ?: message.locationData,
                updatedBackgroundColor ?: message.backgroundColor,
                updatedFontColor ?: message.fontColor,
                updatedFontFamily ?: message.fontFamily
        )
    }

    @Test
    fun `messageDTO to message entity`() {
        val expected = getMessageEntity()
        val result = getMessageDTO(
                updatedLinks = arrayListOf("https://www.example.com", "example2.com")
        ).toEntity()

        assertEquals(result.topic, expected.topic)
        assertEquals(result.sender, expected.sender)
        assertEquals(result.title, expected.title)
        assertEquals(result.content, expected.content)
        assertEquals(result.starttime, expected.starttime)
        assertEquals(result.endtime, expected.endtime)
        assertEquals(result.properties, expected.properties)
        assertNotNull(result.attachment)
        assertNotNull(expected.attachment)
        assertTrue(result.attachment!!.contentEquals(expected.attachment!!))
        assertNotNull(result.logoAttachment)
        assertNotNull(expected.logoAttachment)
        assertTrue(result.logoAttachment!!.contentEquals(expected.logoAttachment!!))
        assertEquals(result.links, expected.links)
        assertEquals(result.locationData, expected.locationData)
        assertEquals(result.backgroundColor, expected.backgroundColor)
        assertEquals(result.fontColor, expected.fontColor)
        assertEquals(result.fontFamily, expected.fontFamily)

        assertNotNull(result.links)
        assertNull(result.id)
    }

    @Test
    fun `message entity to messageDTO`() {
        val expected = getMessageDTO(
                updatedLinks = arrayListOf("https://www.example.com", "example2.com")
        )
        val result = MessageDTO.toDTO(getMessageEntity())

        assertEquals(result.topic, expected.topic)
        assertEquals(result.sender, expected.sender)
        assertEquals(result.title, expected.title)
        assertEquals(result.content, expected.content)
        assertEquals(result.starttime, expected.starttime)
        assertEquals(result.endtime, expected.endtime)
        assertEquals(result.properties, expected.properties)
        assertNotNull(result.attachment)
        assertNotNull(expected.attachment)
        assertTrue(result.attachment!!.contentEquals(expected.attachment!!))
        assertNotNull(result.logoAttachment)
        assertNotNull(expected.logoAttachment)
        assertTrue(result.logoAttachment!!.contentEquals(expected.logoAttachment!!))
        assertEquals(result.links?.get(0), expected.links?.get(0))
        assertEquals(result.links?.get(1), "https://example2.com")
        assertEquals(result.locationData, expected.locationData)

        assertNotNull(result.links)
    }

    @Test
    fun `complete invalid url results in null`() {
        val invalidUrl = "invalid-url"
        val result = MessageDTO.completeURL(invalidUrl)

        assertNull(result)
    }

    @Test
    fun `complete url without protocol results in valid url`() {
        val urlWithoutPort = "example.com"
        val result = MessageDTO.completeURL(urlWithoutPort)

        assertEquals(result, "https://example.com")
    }

    @Test
    fun `complete valid url results in valid url`() {
        val validUrls = listOf(
                "http://www.example.com",
                "https://www.example.com",
                "http://example.com"
        )

        validUrls.forEach {
            val result = MessageDTO.completeURL(it)
            assertEquals(result, it)
        }
    }

    @Test
    fun `has valid Urls with valid Urls`() {
        val validUrls = arrayListOf(
                "http://www.example.com",
                "https://www.example.com",
                "http://example.com"
        )
        val messageDto = MessageDTO(
                links = validUrls
        )

        assertTrue(messageDto.hasValidURLs())
    }

    @Test
    fun `has valid Urls with at least one invalid Urls`() {
        val urls = arrayListOf(
                "http://www.example.com",
                "https://www.example.com",
                "http://example"
        )
        val messageDto = MessageDTO(
                links = urls
        )
        assertFalse(messageDto.hasValidURLs())
    }

    @Test
    fun `invalid hex colors results in false`() {
        val hexColors = listOf(
                "#gggggg",
                "ffffff",
                "00aa",
                "#ffffffaa"
        )
        hexColors.forEach {
            assertFalse(MessageDTO.isValidHexColorString(it))
        }
    }

    @Test
    fun `valid hex color results in true`() {
        val hexColor = "#0088ff"
        assertTrue(MessageDTO.isValidHexColorString(hexColor))
    }

    @Test
    fun `missing topic and properties results in Error`() {
        val dto = getMessageDTO()
        dto.topic = null
        dto.properties = null

        val errors = dto.getErrors()

        assertNotNull(errors)

        assertNotNull(errors!!.topicError)
        assertTrue(errors.topicError!!.isNotBlank())

        assertNull(errors.senderError)
        assertNull(errors.titleError)
        assertNull(errors.contentError)
        assertNull(errors.locationError)
        assertNull(errors.linkError)
        assertNull(errors.backgroundColorError)
        assertNull(errors.fontColorError)
    }

    @Test
    fun `only missing topic results in no Error`() {
        val dto = getMessageDTO()
        dto.topic = null

        val errors = dto.getErrors()

        assertNull(errors)
    }

    @Test
    fun `only missing properties results in no Error`() {
        val dto = getMessageDTO()
        dto.properties = null

        val errors = dto.getErrors()

        assertNull(errors)
    }

    @Test
    fun `missing sender results in Error`() {
        val dto = getMessageDTO()
        dto.sender = null

        val errors = dto.getErrors()

        assertNotNull(errors)

        assertNotNull(errors!!.senderError)
        assertTrue(errors.senderError!!.isNotBlank())

        assertNull(errors.topicError)
        assertNull(errors.titleError)
        assertNull(errors.contentError)
        assertNull(errors.locationError)
        assertNull(errors.linkError)
        assertNull(errors.backgroundColorError)
        assertNull(errors.fontColorError)
    }

    @Test
    fun `missing title results in Error`() {
        val dto = getMessageDTO()
        dto.title = null

        val errors = dto.getErrors()

        assertNotNull(errors)

        assertNotNull(errors!!.titleError)
        assertTrue(errors.titleError!!.isNotBlank())

        assertNull(errors.topicError)
        assertNull(errors.senderError)
        assertNull(errors.contentError)
        assertNull(errors.locationError)
        assertNull(errors.linkError)
        assertNull(errors.backgroundColorError)
        assertNull(errors.fontColorError)
    }

    @Test
    fun `missing content and attachment results in Error`() {
        val dto = getMessageDTO()
        dto.content = null
        dto.attachment = null

        val errors = dto.getErrors()

        assertNotNull(errors)

        assertNotNull(errors!!.contentError)
        assertTrue(errors.contentError!!.isNotBlank())

        assertNull(errors.topicError)
        assertNull(errors.senderError)
        assertNull(errors.titleError)
        assertNull(errors.locationError)
        assertNull(errors.linkError)
        assertNull(errors.backgroundColorError)
        assertNull(errors.fontColorError)
    }

    @Test
    fun `only missing content results in no Error`() {
        val dto = getMessageDTO()
        dto.content = null

        val errors = dto.getErrors()

        assertNull(errors)
    }

    @Test
    fun `only missing attachment results in no Error`() {
        val dto = getMessageDTO()
        dto.attachment = null

        val errors = dto.getErrors()

        assertNull(errors)
    }

    @Test
    fun `longitude out of valid range results in Error`() {
        val dto = getMessageDTO(
                updatedLocationData = LocationData(null, 0.0, 181.0, 10)
        )

        val errors = dto.getErrors()

        assertNotNull(errors)

        assertNotNull(errors!!.locationError)
        assertTrue(errors.locationError!!.isNotBlank())

        assertNull(errors.topicError)
        assertNull(errors.senderError)
        assertNull(errors.titleError)
        assertNull(errors.contentError)
        assertNull(errors.linkError)
        assertNull(errors.backgroundColorError)
        assertNull(errors.fontColorError)
    }

    @Test
    fun `latitude out of valid range results in Error`() {
        val dto = getMessageDTO(
                updatedLocationData = LocationData(null, 91.0, 0.0, 10)
        )

        val errors = dto.getErrors()

        assertNotNull(errors)

        assertNotNull(errors!!.locationError)
        assertTrue(errors.locationError!!.isNotBlank())

        assertNull(errors.topicError)
        assertNull(errors.senderError)
        assertNull(errors.titleError)
        assertNull(errors.contentError)
        assertNull(errors.linkError)
        assertNull(errors.backgroundColorError)
        assertNull(errors.fontColorError)
    }

    @Test
    fun `latitude and longitude in valid range results in no Error`() {
        val dto = getMessageDTO(
                updatedLocationData = LocationData(null, 90.0, 180.0, 10)
        )

        val errors = dto.getErrors()

        assertNull(errors)
    }

    @Test
    fun `invalid links result in Error`() {
        val dto = getMessageDTO(
                updatedLinks = arrayListOf("invalid-link")
        )

        val errors = dto.getErrors()

        assertNotNull(errors)

        assertNotNull(errors!!.linkError)
        assertTrue(errors.linkError!!.isNotBlank())

        assertNull(errors.topicError)
        assertNull(errors.senderError)
        assertNull(errors.titleError)
        assertNull(errors.contentError)
        assertNull(errors.locationError)
        assertNull(errors.backgroundColorError)
        assertNull(errors.fontColorError)
    }

    @Test
    fun `invalid hex color for backgroundColor results in Error`() {
        val dto = getMessageDTO(
                updatedBackgroundColor = "ffffff"
        )

        val errors = dto.getErrors()

        assertNotNull(errors)

        assertNotNull(errors!!.backgroundColorError)
        assertTrue(errors.backgroundColorError!!.isNotBlank())

        assertNull(errors.topicError)
        assertNull(errors.senderError)
        assertNull(errors.titleError)
        assertNull(errors.contentError)
        assertNull(errors.locationError)
        assertNull(errors.linkError)
        assertNull(errors.fontColorError)
    }

    @Test
    fun `invalid hex color for fontColor results in Error`() {
        val dto = getMessageDTO(
                updatedFontColor = "#ffffffff"
        )

        val errors = dto.getErrors()

        assertNotNull(errors)

        assertNotNull(errors!!.fontColorError)
        assertTrue(errors.fontColorError!!.isNotBlank())

        assertNull(errors.topicError)
        assertNull(errors.senderError)
        assertNull(errors.titleError)
        assertNull(errors.contentError)
        assertNull(errors.locationError)
        assertNull(errors.linkError)
        assertNull(errors.backgroundColorError)
    }

    @Test
    fun `getErrors on valid dto results in null`() {
        val dto = MessageDTO.toDTO(getMessageEntity())

        val errors = dto.getErrors()

        assertNull(errors)
    }
}