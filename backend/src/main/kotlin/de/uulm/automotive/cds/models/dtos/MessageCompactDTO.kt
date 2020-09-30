package de.uulm.automotive.cds.models.dtos

import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.models.DTO
import de.uulm.automotive.cds.models.DTOCompanion
import de.uulm.automotive.cds.models.Entity
import org.modelmapper.ModelMapper
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
) : DTO {
    companion object : DTOCompanion {
        override var mapper: ModelMapper = ModelMapper()

        /**
         * Maps the Message entity to the corresponding DTO object
         *
         * @param Message class of the entity
         * @param entity Message entity
         * @return Mapped DTO
         */
        override fun <Message : Entity> toDTO(entity: Message): MessageCompactDTO {
            return mapper.map(entity, MessageCompactDTO::class.java)
        }

    }

    /**
     * Maps the fields of this DTO to the corresponding Entity
     *
     * @return Mapped Message entity
     */
    override fun toEntity(): Message {
        return mapper.map(this, Message::class.java)
    }

}