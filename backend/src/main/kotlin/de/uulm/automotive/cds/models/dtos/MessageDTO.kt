package de.uulm.automotive.cds.models.dtos

import de.uulm.automotive.cds.entities.LocationData
import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.models.*
import de.uulm.automotive.cds.models.errors.MessageBadRequestInfo
import org.modelmapper.ModelMapper
import java.net.URL
import java.time.LocalDateTime

/**
 * Data Transfer Object Representation of the Message entity that is used via the api
 *
 */
data class MessageDTO(
        var topic: String? = null,
        var sender: String? = null,
        var title: String? = null,
        var content: String? = null,
        var starttime: LocalDateTime? = null,
        var endtime: LocalDateTime? = null,
        var properties: MutableList<String>? = null,
        var attachment: ByteArray? = null,
        var logoAttachment: ByteArray? = null,
        var links: MutableList<String>? = null,
        var locationData: LocationData? = null,
        var backgroundColor: String? = null,
        var fontColor: String? = null,
        var fontFamily: FontFamily? = null
) : DTO, ValidateDTO {
    companion object : DTOCompanion {
        override var mapper: ModelMapper = ModelMapper()

        init {
            mapper.addConverter<String, URL> { ctx -> URL(completeURL(ctx.source)) }
        }

        val regexUrl: String = "[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=,!]*)"
        val regexUrlWithoutProtocol: Regex = Regex("(www\\.)?$regexUrl")
        val regexUrlHttp: Regex = Regex("http?:\\/\\/(www\\.)?$regexUrl")
        val regexUrlHttps: Regex = Regex("https?:\\/\\/(www\\.)?$regexUrl")

        val regexHexColor: Regex = Regex("#[A-Fa-f0-9]{6}")

        /**
         * Maps the Message entity to the corresponding DTO object
         *
         * @param Message class of the entity
         * @param entity Message entity
         * @return Mapped DTO
         */
        override fun <Message : Entity> toDTO(entity: Message): MessageDTO {
            return mapper.map(entity, MessageDTO::class.java)
        }

        /**
         * Checks if the given url is valid or incomplete. If it is incomplete the protocol gets
         * prepended.
         *
         * @param url string
         * @return url as string if a valid url can be created, returns null if not
         */
        fun completeURL(url: String): String? {
            if (url.contains(regexUrlWithoutProtocol)) {
                if (url.matches(regexUrlWithoutProtocol)) {
                    return "https://" + url
                } else if (url.matches(regexUrlHttp) || url.matches(regexUrlHttps)) {
                    return url
                }
            }
            return null
        }

        /**
         * Checks if the given string contains a valid hex color.
         * A valid hex color starts with a # and is followed by 6 hex values [0-9a-f]
         *
         * @param hexColor string
         * @return true if it is a valid hex color
         */
        fun isValidHexColorString(hexColor: String): Boolean {
            return hexColor.matches(regexHexColor)
        }
    }

    /**
     * Maps the fields of this DTO to the corresponding Entity
     *
     * @return Mapped Message entity
     */
    override fun toEntity(): Message {
        val entity = mapper.map(this, Message::class.java)

        return entity
    }

    /**
     * Validates all the fields of the DTO. If errors are present, an error object gets created.
     * The found errors are then saved inside the error object.
     *
     * @return error object containing all the errors or null if the DTO is valid
     */
    override fun getErrors(): MessageBadRequestInfo? {
        var errors: MessageBadRequestInfo? = null

        if (sender.isNullOrBlank()) {
            errors = errors ?: MessageBadRequestInfo()
            errors.senderError = "Sender field is required."
        }

        if (title?.isNotEmpty() != true) {
            errors = errors ?: MessageBadRequestInfo()
            errors.titleError = "Title field is required."
        }

        if (topic?.isNotEmpty() != true && properties?.isNotEmpty() != true) {
            errors = errors ?: MessageBadRequestInfo()
            errors.topicError = "Either Topics or Properties are required."
        }

        if (content?.isNotEmpty() != true && attachment?.isNotEmpty() != true) {
            errors = errors ?: MessageBadRequestInfo()
            errors.contentError = "Either Content or Files are required."
        }

        if (!hasValidURLs()) {
            errors = errors ?: MessageBadRequestInfo()
            errors.linkError = "Please check your link values."
        }

        if (backgroundColor != null && !isValidHexColorString(backgroundColor!!)) {
            errors = errors ?: MessageBadRequestInfo()
            errors.backgroundColorError = "Please enter a valid HexColor."
        }

        if (fontColor != null && !isValidHexColorString(fontColor!!)) {
            errors = errors ?: MessageBadRequestInfo()
            errors.fontColorError = "Please enter a valid HexColor."
        }

        locationData?.let {
            if (it.lat < -90 || it.lat > 90 || it.lng > 180 || it.lng < -180) {
                errors = errors ?: MessageBadRequestInfo()
                errors!!.locationError = "Please check your coordinate values!"
            }
        }

        return errors
    }

    /**
     * Checks if all the urls saved in links are valid
     *
     * @return true if all urls are valid
     */
    fun hasValidURLs(): Boolean {
        links?.forEach {
            if (!it.contains(regexUrlHttp) && !it.contains(regexUrlHttps)) {
                return false
            }
        }
        return true
    }
}