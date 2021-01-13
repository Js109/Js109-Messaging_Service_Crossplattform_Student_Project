package de.uulm.automotive.cds.models.errors

import de.uulm.automotive.cds.models.BadRequestInfo
import de.uulm.automotive.cds.models.addErrorGeneric

/**
 * Contains the Error Information for the PropertyDTO.
 *
 * @property nameError used if name is blank
 */
class PropertyBadRequestInfo(
        var nameError: String? = null
) : BadRequestInfo()

fun PropertyBadRequestInfo?.addError(addError: (err: PropertyBadRequestInfo) -> Unit): PropertyBadRequestInfo =
        addErrorGeneric(addError, PropertyBadRequestInfo())