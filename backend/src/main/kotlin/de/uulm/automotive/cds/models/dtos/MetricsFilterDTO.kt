package de.uulm.automotive.cds.models.dtos

import de.uulm.automotive.cds.models.ValidateDTO
import de.uulm.automotive.cds.models.errors.MetricsFilterBadRequestInfo
import de.uulm.automotive.cds.models.errors.addError
import java.time.LocalDate

class MetricsFilterDTO(
        var topicID: Long? = null,
        var propertyID: Long? = null,
        var timeSpanBegin: LocalDate? = null,
        var timeSpanEnd: LocalDate? = null
) : ValidateDTO {
    override fun getErrors(): MetricsFilterBadRequestInfo? {
        var errors: MetricsFilterBadRequestInfo? = null

        if (topicID != null && propertyID != null) {
            errors = errors.addError { it.keyError = "Only one of topicID or propertyID may be set!" }
        }

        if (timeSpanBegin != null && timeSpanEnd != null && timeSpanBegin!!.isAfter(timeSpanEnd)) {
            errors = errors.addError { it.timeSpanError = "The begin of the time span has to be before the end!" }
        }

        return errors
    }

}