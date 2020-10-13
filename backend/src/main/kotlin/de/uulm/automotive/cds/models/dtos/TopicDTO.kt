package de.uulm.automotive.cds.models.dtos

import de.uulm.automotive.cds.entities.Topic
import de.uulm.automotive.cds.models.DTO
import de.uulm.automotive.cds.models.EntityConverter
import de.uulm.automotive.cds.models.ValidateDTO
import de.uulm.automotive.cds.models.errors.TopicBadRequestInfo
import de.uulm.automotive.cds.models.errors.addError

/**
 * Data Transfer Object Representation of the Topic entity that is used via the api
 *
 */
data class TopicDTO(
        var title: String = "",
        var tags: MutableList<String> = arrayListOf(),
        var description: String = ""
) : DTO<Topic>(), ValidateDTO {

    companion object : EntityConverter<Topic, TopicDTO>(
            Topic::class.java,
            TopicDTO::class.java
    )

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
        } else if (title.length > 200) {
            errors = errors.addError { it.titleError = "Title can not contain more than 200 letters" }
        }

        if (description.isBlank()) {
            errors = errors.addError { it.descriptionError = "Description can not be blank." }
        }

        return errors
    }
}