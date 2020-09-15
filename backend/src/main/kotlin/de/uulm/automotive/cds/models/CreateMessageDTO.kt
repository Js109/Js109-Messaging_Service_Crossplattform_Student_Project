package de.uulm.automotive.cds.models

import de.uulm.automotive.cds.entities.LocationData
import de.uulm.automotive.cds.entities.Message
import org.modelmapper.ModelMapper
import java.time.LocalDateTime

data class CreateMessageDTO (
        var topic: String?,
        var sender: String?,
        var title: String?,
        var content: String?,
        var starttime: LocalDateTime?,
        var endtime: LocalDateTime?,
        var properties: MutableList<String>?,
        var attachment: ByteArray?,
        var links: MutableList<String>?, //TODO Mapping
        var locationData: LocationData?
) : DTO {
    companion object :DTOCompanion {
        override val mapper: ModelMapper = ModelMapper()

        override fun <Message : Entity> toDTO(entity: Message): CreateMessageDTO {
            return mapper.map(entity, CreateMessageDTO::class.java)
        }
    }

    override fun toEntity(): Message {
        return mapper.map(this, Message::class.java)
    }

    override fun isValid(): Boolean {
        TODO("Not yet implemented")
    }
}