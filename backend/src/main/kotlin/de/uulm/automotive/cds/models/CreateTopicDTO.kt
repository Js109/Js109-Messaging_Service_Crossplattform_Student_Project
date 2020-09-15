package de.uulm.automotive.cds.models

import de.uulm.automotive.cds.entities.Topic
import org.modelmapper.ModelMapper

/**
 * Data Transfer Object Representation of the Topic entity that is used for the creation of new
 * Topic objects via the API
 *
 */
data class CreateTopicDTO(
        var binding: String = "",
        var title: String = "",
        var tags: MutableList<String> = arrayListOf(),
        var description: String = ""
) : DTO {
    companion object : DTOCompanion {
        override val mapper: ModelMapper = ModelMapper()

        /**
         * Maps the Topic entity to the corresponding DTO object
         *
         * @param Topic class of the entity
         * @param entity Topic entity
         * @return Mapped DTO
         */
        override fun <Topic : Entity> toDTO(entity: Topic): CreateTopicDTO {
            return mapper.map(entity, CreateTopicDTO::class.java)
        }
    }

    /**
     * Maps the fields of this DTO to the corresponding Entity
     *
     * @return Mapped Topic entity
     */
    override fun toEntity() : Topic {
        return mapper.map(this, Topic::class.java)
    }

    /**
     * Validates this object
     *
     * @return true if this object is valid
     */
    override fun isValid(): Boolean {
        return validateBinding()
                && validateTitle()
                && validateDescription()
    }

    /**
     * Checks if the Binding is not blank
     *
     * @return true if the Binding is not blank
     */
    private fun validateBinding(): Boolean {
        return binding.isNotBlank()
    }

    /**
     * Checks if the Title is not blank
     *
     * @return true if the Title is not blank
     */
    private fun validateTitle(): Boolean {
        return title.isNotBlank()
    }

    /**
     * Checks if the Description is not blank
     *
     * @return true if the Description is not blank
     */
    private fun validateDescription(): Boolean {
        return description.isNotBlank()
    }
}