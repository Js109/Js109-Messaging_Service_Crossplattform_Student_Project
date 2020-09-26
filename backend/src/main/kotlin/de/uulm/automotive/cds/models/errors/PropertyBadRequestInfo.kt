package de.uulm.automotive.cds.models.errors

import de.uulm.automotive.cds.models.BadRequestInfo

/**
 * Contains the Error Information for the PropertyDTO.
 *
 * @property bindingError used if binding is blank
 * @property nameError used if name is blank
 */
class PropertyBadRequestInfo(
        var bindingError: String? = null,
        var nameError: String? = null
) : BadRequestInfo()