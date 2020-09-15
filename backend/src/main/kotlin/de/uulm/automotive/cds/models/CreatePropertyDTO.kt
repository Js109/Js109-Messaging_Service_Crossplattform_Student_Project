package de.uulm.automotive.cds.models

import de.uulm.automotive.cds.entities.Property
import org.modelmapper.ModelMapper

/**
 * Data Transfer Object Representation of the Property entity that is used for the creation of new
 * Topic objects via the API
 *
 */
data class CreatePropertyDTO  (
    var name: String = "",
    var binding: String = ""
) : DTO {
    companion object : DTOCompanion {
        override val mapper: ModelMapper = ModelMapper()

        /**
         * Maps the Property entity to the corresponding DTO object
         *
         * @param Property class of the entity
         * @param entity Property entity
         * @return Mapped DTO
         */
        override fun <Property : Entity> toDTO(entity: Property): CreatePropertyDTO {
            return mapper.map(entity, CreatePropertyDTO::class.java)
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
     * Validates this object
     *
     * @return true if this object is valid
     */
    override fun isValid(): Boolean {
        return validateName()
                && validateBinding()
    }

    /**
     * Checks if the Name is not blank
     *
     * @return true if the Binding is not blank
     */
    private fun validateName() : Boolean {
        return name.isNotBlank()
    }

    /**
     * Checks if the Binding is not blank
     *
     * @return true if the Binding is not blank
     */
    private fun validateBinding() : Boolean {
        return binding.isNotBlank()
    }
}