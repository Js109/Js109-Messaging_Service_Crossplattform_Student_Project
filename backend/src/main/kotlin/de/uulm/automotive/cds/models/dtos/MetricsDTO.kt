package de.uulm.automotive.cds.models.dtos

import java.time.DayOfWeek
import java.time.LocalDate

class MetricsDTO(
        var sentMessagesTotalAllTime: Int? = null,
        var sentMessagesTotalAtBegin: Int? = null,
        var sentMessagesTotalGain: Int? = null,
        var scheduledMessagesTotalAllTime: Int? = null,
        var scheduledMessagesTimeSpan: Int? = null,
        var subscriberTotalAllTime: Int? = null,
        var subscriberTotalAtBegin: Int? = null,
        var subscriberTotalGainOverTimespan: Int? = null,
        var averageMessageLengthAllTime: Double? = null,
        var averageMessageLengthTimeSpan: Double? = null,
        var mostActiveSenderAllTime: String? = null,
        var mostActiveSenderTimeSpan: String? = null,
        var mostActiveWeekdayAllTime: DayOfWeek? = null,
        var mostActiveWeekdayTimeSpan: DayOfWeek? = null,
        var sentMessagesByTimeOfDayAllTime: Map<Int, Int> = mapOf(),
        var sentMessagesByTimeOfDayTimeSpan: Map<Int, Int> = mapOf(),
        var sentMessagesByDateTimeSpan: Map<LocalDate, Int> = mapOf(),
        var scheduledMessagesByDateTimeSpan: Map<LocalDate, Int> = mapOf(),
        var subscriberGainByDateTimeSpan: Map<LocalDate, Int> = mapOf()
)