package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.models.dtos.MetricsDTO
import de.uulm.automotive.cds.models.dtos.MetricsFilterDTO
import de.uulm.automotive.cds.repositories.MessageRepository
import de.uulm.automotive.cds.services.MetricsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.DayOfWeek
import java.time.LocalDate

@CrossOrigin("*")
@RestController
@RequestMapping("/metrics")
/**
 * REST-Endpoint for Metrics.
 */
class MetricsController(private val repository: MessageRepository, private val metricsService: MetricsService) {

    @GetMapping("/topicSubscriptionDistribution")
    fun showTopicSubscriptionDistribution(): Map<String, Int> =
            metricsService.getTopicSubscriptionDistribution()

    @GetMapping("/propertySubscriptionDistribution")
    fun showPropertySubscriptionDistribution(): Map<String, Int> =
            metricsService.getPropertySubscriptionDistribution()


    @PostMapping
    fun showFilteredMetrics(@RequestBody metricsFilter: MetricsFilterDTO): ResponseEntity<Any> {
        val errors = metricsFilter.getErrors()

        if (errors != null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors)
        }

        return ResponseEntity.status(HttpStatus.OK).body(MetricsDTO(
                337,
                53,
                25,
                50,
                8,
                596.9,
                "Test OEM",
                DayOfWeek.SUNDAY,
                arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 2, 5, 15, 3, 8, 11, 2, 1, 1, 0, 0, 0, 0, 0, 1, 0),
                mapOf(
                        LocalDate.of(2020, 10, 3) to 1,
                        LocalDate.of(2020, 10, 4) to 0,
                        LocalDate.of(2020, 10, 5) to 17,
                        LocalDate.of(2020, 10, 6) to 5,
                        LocalDate.of(2020, 10, 7) to 13
                ),
                mapOf(
                        LocalDate.of(2020, 10, 8) to 9,
                        LocalDate.of(2020, 10, 9) to 4,
                        LocalDate.of(2020, 10, 10) to 1,
                        LocalDate.of(2020, 10, 11) to 0,
                        LocalDate.of(2020, 10, 12) to 14
                ),
                mapOf(
                        LocalDate.of(2020, 10, 3) to 5,
                        LocalDate.of(2020, 10, 4) to 5,
                        LocalDate.of(2020, 10, 5) to 6,
                        LocalDate.of(2020, 10, 6) to 5,
                        LocalDate.of(2020, 10, 7) to 3
                )
        ))
    }
}