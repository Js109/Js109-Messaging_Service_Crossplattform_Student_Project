package de.uulm.automotive.cds.services

import de.uulm.automotive.cds.repositories.MessageRepository
import de.uulm.automotive.cds.repositories.PropertyRepository
import de.uulm.automotive.cds.repositories.TopicRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MetricsService @Autowired constructor(
        val messageRepository: MessageRepository,
        val propertyRepository: PropertyRepository,
        val topicRepository: TopicRepository
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
}