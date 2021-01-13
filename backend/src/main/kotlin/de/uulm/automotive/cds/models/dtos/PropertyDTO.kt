package de.uulm.automotive.cds.models.dtos

import de.uulm.automotive.cds.entities.Property
import de.uulm.automotive.cds.models.DTO
import de.uulm.automotive.cds.models.EntityConverter
import de.uulm.automotive.cds.models.ValidateDTO
import de.uulm.automotive.cds.models.errors.PropertyBadRequestInfo
import de.uulm.automotive.cds.models.errors.addError

/**
 * Data Transfer Object Representation of the Property entity that is used via the api
 *
 */
data class PropertyDTO(
        var name: String = ""
) : DTO<Property>(), ValidateDTO {

    companion object : EntityConverter<Property, PropertyDTO>(
            Property::class.java,
            PropertyDTO::class.java
    )

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
        } else if (name.length > 200) {
            errors = errors.addError { it.nameError = "Name can not contain more than 200 characters." }
        }

        return errors
    }
}