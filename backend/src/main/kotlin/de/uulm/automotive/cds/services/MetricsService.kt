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
        val filterBeforeTimeSpan = MetricsFilterDTO(
                metricsFilter.topicName,
                metricsFilter.propertyName,
                LocalDate.MIN,
                metricsFilter.timeSpanBegin
        )
        val filterAfterTimeSpan = MetricsFilterDTO(
                metricsFilter.topicName,
                metricsFilter.propertyName,
                metricsFilter.timeSpanEnd,
                LocalDate.MAX
        )
        val filteredMessagesTimeSpan = messageService.filterMessagesForMetrics(metricsFilter)
        val filteredMessagesBeforeTimeSpan = messageService.filterMessagesForMetrics(filterBeforeTimeSpan)
        val filteredMessagesAfterTimeSpan = messageService.filterMessagesForMetrics(filterAfterTimeSpan)

        return MetricsDTO(
                filteredMessagesBeforeTimeSpan
                        .filter { it.isSent == true }
                        .count(),
                filteredMessagesTimeSpan
                        .filter { it.isSent == true }
                        .count(),
                filteredMessagesBeforeTimeSpan
                        .union(filteredMessagesTimeSpan)
                        .union(filteredMessagesAfterTimeSpan)
                        .filter { it.isSent == false }
                        .count(),
                50,
                8,
                filteredMessagesTimeSpan
                        .map { it.content?.length ?: 0 }
                        .average(),
                filteredMessagesTimeSpan
                        .map { it.sender }
                        .groupingBy { it }
                        .eachCount()
                        .maxBy { it.value }?.key,
                filteredMessagesTimeSpan
                        .map { it.starttime?.dayOfWeek }
                        .groupingBy { it }
                        .eachCount()
                        .maxBy { it.value }?.key,
                filteredMessagesTimeSpan
                        .filter { it.isSent == true }
                        .map { it.starttime?.hour }
                        .groupingBy { it }
                        .eachCount()
                        .filter { it.key != null } as Map<Int, Int>,
                filteredMessagesTimeSpan
                        .filter { it.isSent == true }
                        .map { it.starttime?.toLocalDate() }
                        .groupingBy { it }
                        .eachCount()
                        .filter { it.key != null } as Map<LocalDate, Int>,
                filteredMessagesTimeSpan
                        .filter { it.isSent == false }
                        .map { it.starttime?.toLocalDate() }
                        .groupingBy { it }
                        .eachCount()
                        .filter { it.key != null } as Map<LocalDate, Int>,
                mapOf(
                        LocalDate.of(2020, 10, 3) to 5,
                        LocalDate.of(2020, 10, 4) to 5,
                        LocalDate.of(2020, 10, 5) to 6,
                        LocalDate.of(2020, 10, 6) to 5,
                        LocalDate.of(2020, 10, 7) to 3
                )
        )
    }
}