package de.uulm.automotive.cds.services

import de.uulm.automotive.cds.Message
import de.uulm.automotive.cds.MessageRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SchedulingService(private val repository: MessageRepository) {

    @Scheduled(fixedRate = 60000)
    fun sendMessages(): Boolean {
        val messages = repository.findAllByIsSentFalseOrderByStarttimeAsc()
        messages.forEach { message: Message ->
            if (message.starttime!! > LocalDateTime.now().plusHours(1)) return true

            val messageService = MessageService()
            messageService.sendMessage(message)

            message.isSent = true
            repository.save(message)
        }
        return true
    }
}