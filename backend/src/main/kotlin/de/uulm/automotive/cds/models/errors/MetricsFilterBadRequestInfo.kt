package de.uulm.automotive.cds.models.errors

import de.uulm.automotive.cds.models.BadRequestInfo
import de.uulm.automotive.cds.models.addErrorGeneric

class MetricsFilterBadRequestInfo(
        var keyError: String? = null,
        var timeSpanError: String? = null
) : BadRequestInfo()

fun MetricsFilterBadRequestInfo?.addError(addError: (err: MetricsFilterBadRequestInfo) -> Unit): MetricsFilterBadRequestInfo =
        addErrorGeneric(addError, MetricsFilterBadRequestInfo())