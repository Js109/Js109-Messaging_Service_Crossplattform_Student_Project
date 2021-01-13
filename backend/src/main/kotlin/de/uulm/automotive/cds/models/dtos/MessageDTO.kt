package de.uulm.automotive.cds.models.dtos

import de.uulm.automotive.cds.entities.LocationData
import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.models.DTO
import de.uulm.automotive.cds.models.EntityConverter
import de.uulm.automotive.cds.models.ValidateDTO
import de.uulm.automotive.cds.models.errors.MessageBadRequestInfo
import de.uulm.automotive.cds.models.errors.addError
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
        var messageDisplayProperties: MessageDisplayPropertiesDTO? = null
) : DTO<Message>(), ValidateDTO {
    companion object : EntityConverter<Message, MessageDTO>(
            Message::class.java,
            MessageDTO::class.java
    ) {
        val regexUrl: String = "[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=,!]*)"
        val regexUrlWithoutProtocol: Regex = Regex("(www\\.)?$regexUrl")
        val regexUrlHttp: Regex = Regex("http?:\\/\\/(www\\.)?$regexUrl")
        val regexUrlHttps: Regex = Regex("https?:\\/\\/(www\\.)?$regexUrl")

        val regexHexColor: Regex = Regex("#[A-Fa-f0-9]{6}")

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
     * Validates all the fields of the DTO. If errors are present, an error object gets created.
     * The found errors are then saved inside the error object.
     *
     * @return error object containing all the errors or null if the DTO is valid
     */
    override fun getErrors(): MessageBadRequestInfo? {
        var errors: MessageBadRequestInfo? = null

        if (sender.isNullOrBlank()) {
            errors = errors.addError { it.senderError = "Sender field is required" }
        } else if (sender!!.length > 127) {
            errors = errors.addError { it.senderError = "Sender can not contain more than 127 characters." }
        }

        if (title.isNullOrBlank()) {
            errors = errors.addError { it.titleError = "Title field is required." }
        } else if (title!!.length > 127) {
            errors = errors.addError { it.titleError = "Title can not contain more than 127 characters." }
        }

        if (!(topic.isNullOrBlank().xor(properties.isNullOrEmpty()))) {
            errors = errors.addError { it.topicError = "Either Topics or Properties are required. Please select only one of both." }
        }
        if (topic != null && topic!!.length > 200) {
            errors = errors.addError { it.topicError = "Topic can not contain more than 200 characters." }
        }

        properties?.forEach {
            if (it.length > 200) {
                errors = errors.addError { err ->  err.propertyError = "Property can not contain more than 200 characters." }
            }
        }

        if (content.isNullOrEmpty() && (attachment == null || attachment!!.isEmpty())) {
            errors = errors.addError { it.contentError = "Either Content or Files are required." }
        } else if (content != null && content!!.length > 1023) {
            errors = errors.addError { it.contentError = "Content can not contain more than 1023 characters." }
        }

        if (!hasValidURLs()) {
            errors = errors.addError { it.linkError = "Please check your link values." }
        }

        messageDisplayProperties?.let { dto ->
            dto.getErrors()?.let { displayError ->
                errors = errors.addError {
                    it.colorError = displayError.colorError
                    it.colorFormatError = displayError.colorFormatError
                }
            }
        }

        locationData?.let {
            if (it.lat < -90 || it.lat > 90 || it.lng > 180 || it.lng < -180) {
                errors = errors.addError { err -> err.locationError = "Please check your coordinate values!" }
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