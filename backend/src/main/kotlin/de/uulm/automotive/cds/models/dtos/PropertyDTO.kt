package de.uulm.automotive.cds.models.dtos

import de.uulm.automotive.cds.entities.Property
import de.uulm.automotive.cds.models.DTO
import de.uulm.automotive.cds.models.DTOCompanion
import de.uulm.automotive.cds.models.Entity
import de.uulm.automotive.cds.models.ValidateDTO
import de.uulm.automotive.cds.models.errors.PropertyBadRequestInfo
import org.modelmapper.ModelMapper
import de.uulm.automotive.cds.models.errors.addError

/**
 * Data Transfer Object Representation of the Property entity that is used via the api
 *
 */
data class PropertyDTO(
        var name: String = ""
) : DTO, ValidateDTO {
    companion object : DTOCompanion {
        override val mapper: ModelMapper = ModelMapper()

        /**
         * Maps the Property entity to the corresponding DTO object
         *
         * @param Property class of the entity
         * @param entity Property entity
         * @return Mapped DTO
         */
        override fun <Property : Entity> toDTO(entity: Property): PropertyDTO {
            return mapper.map(entity, PropertyDTO::class.java)
        }
    }

    /**
     * Maps the fields of this DTO to the corresponding Entity
     *
     * @return Mapped Property entity
     */
    override fun toEntity(): Property {
        return mapper.map(this, Property::class.java)
    }

    /**
     * Validates all the fields of the DTO. If errors are present, an error object gets created.
     * The found errors are then saved inside the error object.
     *
     * @return error object containing all the errors or null if the DTO is valid
     */
    override fun getErrors(): PropertyBadRequestInfo? {
        var errors: PropertyBadRequestInfo? = null

        if (name.isBlank()) {
            errors = errors.addError { it.nameError = "Name can not be blank." }
        }

        return errors
    }
}