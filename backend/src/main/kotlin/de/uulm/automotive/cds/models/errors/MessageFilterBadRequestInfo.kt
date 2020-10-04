package de.uulm.automotive.cds.models.errors

import de.uulm.automotive.cds.models.BadRequestInfo
import de.uulm.automotive.cds.models.addErrorGeneric

/**
 * Contains the Error Information for the MessageFilterDTO.
 *
 * @property DateRangeError used if endtime is before starttime or one of the fields is empty (both are allowed to be empty)
 */
class MessageFilterBadRequestInfo(
        var DateRangeError: String? = null
) : BadRequestInfo()

fun MessageFilterBadRequestInfo?.addError(addError: (err: MessageFilterBadRequestInfo) -> Unit): MessageFilterBadRequestInfo =
        addErrorGeneric(addError, MessageFilterBadRequestInfo())