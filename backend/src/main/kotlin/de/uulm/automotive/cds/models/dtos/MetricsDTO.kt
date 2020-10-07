package de.uulm.automotive.cds.models.dtos

import java.time.DayOfWeek
import java.time.LocalDate

class MetricsDTO(
        var sentMessagesTotalAtBegin: Int? = null,
        var sentMessagesTotalGain: Int? = null,
        var scheduledMessages: Int? = null,
        var subscriberTotalAtBegin: Int? = null,
        var subscriberTotalGainOverTimespan: Int? = null,
        var averageMessageLength: Double? = null,
        var mostActiveSender: String? = null,
        var mostActiveWeekday: DayOfWeek? = null,
        var sentMessagesByTimeOfDay: Array<Int> = arrayOf(),
        var sentMessagesByDate: Map<LocalDate, Int> = mapOf(),
        var scheduledMessagesByDate: Map<LocalDate, Int> = mapOf(),
        var subscriberGainByDate: Map<LocalDate, Int> = mapOf()
)