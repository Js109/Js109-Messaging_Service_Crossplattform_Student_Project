package de.uulm.automotive.cds.services

import de.uulm.automotive.cds.models.dtos.MetricsDTO
import de.uulm.automotive.cds.models.dtos.MetricsFilterDTO
import de.uulm.automotive.cds.repositories.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class MetricsService @Autowired constructor(
        val messageRepository: MessageRepository,
        val messageService: MessageService
) {

    fun getTopicSubscriptionDistribution(): Map<String, Int> {
        return mapOf(
                "binding/test" to 10,
                "binding/Kultur" to 5,
                "binding/Natur" to 3,
                "binding/Alligator" to 12,
                "binding/Werbung" to 7
        )
    }

    fun getPropertySubscriptionDistribution(): Map<String, Int> {
        return mapOf(
                "device/AndroidEmulator" to 15,
                "device/Audi" to 7,
                "device/BMW" to 7,
                "device/Mercedes" to 7
        )
    }

    fun getMetrics(metricsFilter: MetricsFilterDTO): MetricsDTO {
        val filterBeforeTimeSpan =
                metricsFilter.timeSpanBegin?.let { timeSpanBegin ->
                    MetricsFilterDTO(
                            metricsFilter.topicName,
                            metricsFilter.propertyName,
                            timeSpanEnd = timeSpanBegin.minusDays(1)
                    )
                }

        val filterAfterTimeSpan =
                metricsFilter.timeSpanEnd?.let { timeSpanEnd ->
                    MetricsFilterDTO(
                            metricsFilter.topicName,
                            metricsFilter.propertyName,
                            timeSpanBegin = timeSpanEnd.plusDays(1)
                    )
                }

        val filteredMessagesTimeSpan = messageService.filterMessagesForMetrics(metricsFilter)
        val filteredMessagesBeforeTimeSpan =
                when (filterBeforeTimeSpan) {
                    null -> listOf()
                    else -> messageService.filterMessagesForMetrics(filterBeforeTimeSpan)
                }
        val filteredMessagesAfterTimeSpan =
                when (filterAfterTimeSpan) {
                    null -> listOf()
                    else -> messageService.filterMessagesForMetrics(filterAfterTimeSpan)
                }

        val filteredMessagesAllTime = filteredMessagesBeforeTimeSpan
                .union(filteredMessagesTimeSpan)
                .union(filteredMessagesAfterTimeSpan)

        return MetricsDTO(
                sentMessagesTotalAllTime = filteredMessagesAllTime
                        .filter { it.isSent == true }
                        .count(),
                sentMessagesTotalAtBegin = filteredMessagesBeforeTimeSpan
                        .filter { it.isSent == true }
                        .count(),
                sentMessagesTotalGain = filteredMessagesTimeSpan
                        .filter { it.isSent == true }
                        .count(),
                scheduledMessagesTotalAllTime = filteredMessagesAllTime
                        .filter { it.isSent == false }
                        .count(),
                scheduledMessagesTimeSpan = filteredMessagesTimeSpan
                        .filter { it.isSent == false }
                        .count(),
                subscriberTotalAllTime = 55,
                subscriberTotalAtBegin = 50,
                subscriberTotalGainOverTimespan = 8,
                averageMessageLengthAllTime = filteredMessagesAllTime
                        .map { it.content?.length ?: 0 }
                        .average(),
                averageMessageLengthTimeSpan = filteredMessagesTimeSpan
                        .map { it.content?.length ?: 0 }
                        .average(),
                mostActiveSenderAllTime = filteredMessagesAllTime
                        .map { it.sender }
                        .groupingBy { it }
                        .eachCount()
                        .maxBy { it.value }?.key,
                mostActiveSenderTimeSpan = filteredMessagesTimeSpan
                        .map { it.sender }
                        .groupingBy { it }
                        .eachCount()
                        .maxBy { it.value }?.key,
                mostActiveWeekdayAllTime = filteredMessagesAllTime
                        .map { it.starttime?.dayOfWeek }
                        .groupingBy { it }
                        .eachCount()
                        .maxBy { it.value }?.key,
                mostActiveWeekdayTimeSpan = filteredMessagesTimeSpan
                        .map { it.starttime?.dayOfWeek }
                        .groupingBy { it }
                        .eachCount()
                        .maxBy { it.value }?.key,
                sentMessagesByTimeOfDayAllTime = filteredMessagesAllTime
                        .filter { it.isSent == true }
                        .map { it.starttime?.hour }
                        .groupingBy { it }
                        .eachCount()
                        .filter { it.key != null } as Map<Int, Int>,
                sentMessagesByTimeOfDayTimeSpan = filteredMessagesTimeSpan
                        .filter { it.isSent == true }
                        .map { it.starttime?.hour }
                        .groupingBy { it }
                        .eachCount()
                        .filter { it.key != null } as Map<Int, Int>,
                sentMessagesByDateTimeSpan = filteredMessagesTimeSpan
                        .filter { it.isSent == true }
                        .map { it.starttime?.toLocalDate() }
                        .groupingBy { it }
                        .eachCount()
                        .filter { it.key != null } as Map<LocalDate, Int>,
                scheduledMessagesByDateTimeSpan = filteredMessagesTimeSpan
                        .filter { it.isSent == false }
                        .map { it.starttime?.toLocalDate() }
                        .groupingBy { it }
                        .eachCount()
                        .filter { it.key != null } as Map<LocalDate, Int>,
                subscriberGainByDateTimeSpan = mapOf(
                        LocalDate.of(2020, 10, 3) to 5,
                        LocalDate.of(2020, 10, 4) to 5,
                        LocalDate.of(2020, 10, 5) to 6,
                        LocalDate.of(2020, 10, 6) to 5,
                        LocalDate.of(2020, 10, 7) to 3
                )
        )
    }
}