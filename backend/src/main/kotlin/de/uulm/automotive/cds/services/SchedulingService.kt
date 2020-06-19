package de.uulm.automotive.cds.services

import de.uulm.automotive.cds.Message
import de.uulm.automotive.cds.MessageRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * TODO
 *
 * @property repository
 * @property messageService
 */
@Component
class SchedulingService(private val repository: MessageRepository, private val messageService: MessageService) {

    /**
     * TODO
     *
     * @return Boolean
     */
    @Scheduled(fixedRate = 10000)
    fun sendMessages(): Boolean {
        val messages = repository.findAllByIsSentFalseOrderByStarttimeAsc()
        messages.forEach { message: Message ->
            if (message.starttime!! > LocalDateTime.now()) return true

            messageService.sendMessage(message)
            message.isSent = true
            repository.save(message)
        }
        return true
    }
}