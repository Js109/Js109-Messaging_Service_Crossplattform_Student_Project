package de.uulm.automotive.cds.models.dtos

import de.uulm.automotive.cds.entities.MessageDisplayProperties
import de.uulm.automotive.cds.models.*
import de.uulm.automotive.cds.models.errors.MessageDisplayPropertiesBadRequestInfo
import de.uulm.automotive.cds.models.errors.addError


class MessageDisplayPropertiesDTO(
        var backgroundColor: String? = null,
        var fontColor: String? = null,
        var fontFamily: FontFamily? = null,
        var alignment: Alignment? = null
) : DTO<MessageDisplayProperties>(), ValidateDTO {

    companion object : EntityConverter<MessageDisplayProperties, MessageDisplayPropertiesDTO> (
            MessageDisplayProperties::class.java,
            MessageDisplayPropertiesDTO::class.java
    )

    override fun getErrors(): MessageDisplayPropertiesBadRequestInfo? {
        var errors: MessageDisplayPropertiesBadRequestInfo? = null

        if (backgroundColor == fontColor) {
            errors = errors.addError { it.colorError = "Please enter different colors for background and font." }
        }

        if (backgroundColor != null && !MessageDTO.isValidHexColorString(backgroundColor!!)) {
            errors = errors.addError { it.colorFormatError = "Please enter a valid HexColor." }
        }

        if (fontColor != null && !MessageDTO.isValidHexColorString(fontColor!!)) {
            errors = errors.addError { it.colorFormatError = "Please enter a valid HexColor." }
        }

        return errors
    }
}