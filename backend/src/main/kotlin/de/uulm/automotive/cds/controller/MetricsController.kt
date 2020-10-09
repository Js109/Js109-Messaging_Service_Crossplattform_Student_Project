package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.models.dtos.MetricsFilterDTO
import de.uulm.automotive.cds.repositories.MessageRepository
import de.uulm.automotive.cds.services.MetricsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

        return ResponseEntity.status(HttpStatus.OK).body(metricsService.getMetrics(metricsFilter))
    }
}