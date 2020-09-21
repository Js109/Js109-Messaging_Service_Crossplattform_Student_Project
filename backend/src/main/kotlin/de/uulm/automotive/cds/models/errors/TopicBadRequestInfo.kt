package de.uulm.automotive.cds.models.errors

import de.uulm.automotive.cds.models.BadRequestInfo

/**
 * Contains the Error Information for the TopicDTO.
 *
 * @property bindingError Used if Binding is blank
 * @property titleError Used if title is blank
 * @property descriptionError used if description is blank
 */
class TopicBadRequestInfo(
        var bindingError: String? = null,
        var titleError: String? = null,
        var descriptionError: String? = null
) : BadRequestInfo()