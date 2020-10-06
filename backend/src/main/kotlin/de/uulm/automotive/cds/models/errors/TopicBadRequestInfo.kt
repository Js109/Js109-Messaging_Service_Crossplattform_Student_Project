package de.uulm.automotive.cds.models.errors

import de.uulm.automotive.cds.models.BadRequestInfo
import de.uulm.automotive.cds.models.addErrorGeneric

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

fun TopicBadRequestInfo?.addError(addError: (err: TopicBadRequestInfo) -> Unit): TopicBadRequestInfo =
        addErrorGeneric(addError, TopicBadRequestInfo())