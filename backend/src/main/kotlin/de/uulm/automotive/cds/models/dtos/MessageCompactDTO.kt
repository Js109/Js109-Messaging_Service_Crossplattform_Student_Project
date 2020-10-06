package de.uulm.automotive.cds.models.dtos

import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.models.DTO
import de.uulm.automotive.cds.models.EntityConverter
import java.time.LocalDateTime

/**
 * Data Transfer Object Representation of the Message entity that is used via the api for a reduced
 * Message that only contains the necessary fields for a basic message history.
 *
 */
data class MessageCompactDTO(
        var id: Long? = null,
        var sender: String? = null,
        var title: String? = null,
        var content: String? = null,
        var starttime: LocalDateTime? = null,
        var isSent: Boolean? = null
) : DTO<Message>() {
    companion object : EntityConverter<Message, MessageCompactDTO>(
            Message::class.java,
            MessageCompactDTO::class.java
    )
}