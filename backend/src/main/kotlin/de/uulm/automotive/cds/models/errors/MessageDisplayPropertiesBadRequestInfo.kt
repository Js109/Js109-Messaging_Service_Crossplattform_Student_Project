package de.uulm.automotive.cds.models.errors

import de.uulm.automotive.cds.models.BadRequestInfo
import de.uulm.automotive.cds.models.addErrorGeneric

class MessageDisplayPropertiesBadRequestInfo (
        var colorError: String? = null,
        var colorFormatError: String? = null
) : BadRequestInfo()

fun MessageDisplayPropertiesBadRequestInfo?.addError(addError: (err: MessageDisplayPropertiesBadRequestInfo) -> Unit): MessageDisplayPropertiesBadRequestInfo =
        addErrorGeneric(addError, MessageDisplayPropertiesBadRequestInfo())