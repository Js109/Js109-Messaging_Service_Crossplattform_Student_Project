package de.uulm.automotive.cds.models.dtos

import de.uulm.automotive.cds.models.errors.MessageFilterBadRequestInfo
import de.uulm.automotive.cds.models.errors.addError
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Data Transfer Object Representation of the Property entity that is used via the api
 *
 */
data class MessageFilterDTO(
        var searchString: String? = null,
        var startTimePeriod: String? = null,
        var endTimePeriod: String? = null,
        var topic: String? = null
) {
    fun getErrors(): MessageFilterBadRequestInfo? {
        var errors: MessageFilterBadRequestInfo? = null

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateStartTimePeriod = if (startTimePeriod!!.isNotEmpty()) LocalDate.parse(startTimePeriod, formatter).atTime(0, 0) else null
        val dateEndTimePeriod = if (endTimePeriod!!.isNotEmpty()) LocalDate.parse(endTimePeriod, formatter).atTime(23, 59) else null
        if (dateStartTimePeriod!! > dateEndTimePeriod!!) {
            errors = errors.addError { it.DateRangeError = "Starttime must be before Endtime and it's only allowed to use a Daterange, not a single date." }
        }

        return errors
    }
}
