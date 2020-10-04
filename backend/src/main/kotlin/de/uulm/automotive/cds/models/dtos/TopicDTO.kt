package de.uulm.automotive.cds.models.dtos

import de.uulm.automotive.cds.entities.Topic
import de.uulm.automotive.cds.models.DTO
import de.uulm.automotive.cds.models.DTOCompanion
import de.uulm.automotive.cds.models.Entity
import de.uulm.automotive.cds.models.ValidateDTO
import de.uulm.automotive.cds.models.errors.TopicBadRequestInfo
import org.modelmapper.ModelMapper
import de.uulm.automotive.cds.models.errors.addError

/**
 * Data Transfer Object Representation of the Topic entity that is used via the api
 *
 */
data class TopicDTO(
        var title: String = "",
        var tags: MutableList<String> = arrayListOf(),
        var description: String = ""
) : DTO, ValidateDTO {
    companion object : DTOCompanion {
        override val mapper: ModelMapper = ModelMapper()

        /**
         * Maps the Topic entity to the corresponding DTO object
         *
         * @param Topic class of the entity
         * @param entity Topic entity
         * @return Mapped DTO
         */
        override fun <Topic : Entity> toDTO(entity: Topic): TopicDTO {
            return mapper.map(entity, TopicDTO::class.java)
        }
    }

    /**
     * Maps the fields of this DTO to the corresponding Entity
     *
     * @return Mapped Topic entity
     */
    override fun toEntity(): Topic {
        return mapper.map(this, Topic::class.java)
    }

    /**
     * Validates all the fields of the DTO. If errors are present, an error object gets created.
     * The found errors are then saved inside the error object.
     *
     * @return error object containing all the errors or null if the DTO is valid
     */
    override fun getErrors(): TopicBadRequestInfo? {
        var errors: TopicBadRequestInfo? = null


        if (title.isBlank()) {
            errors = errors.addError { it.titleError = "Title can not be blank" }
        }

        if (description.isBlank()) {
            errors = errors.addError { it.descriptionError =  "Description can not be blank." }
        }

        return errors
    }
}