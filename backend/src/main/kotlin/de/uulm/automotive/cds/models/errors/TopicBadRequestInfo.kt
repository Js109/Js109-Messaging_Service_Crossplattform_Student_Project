package de.uulm.automotive.cds.models.errors

import de.uulm.automotive.cds.models.BadRequestInfo

/**
 * Contains the Error Information for the TopicDTO.
 *
 * @property titleError Used if title is blank
 * @property descriptionError used if description is blank
 */
class TopicBadRequestInfo(
        var titleError: String? = null,
        var descriptionError: String? = null,
        var tagError: String? = null
) : BadRequestInfo()