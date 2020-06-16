package de.uulm.automotive.cds.services

import de.uulm.automotive.cds.Message
import de.uulm.automotive.cds.MessageRepository
import de.uulm.automotive.cds.repositories.SignUpRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SchedulingService(private val messageRepository: MessageRepository, private val tokenRepository: SignUpRepository) {

    @Scheduled(fixedRate = 60000)
    fun sendMessages(): Boolean {
        val messages = messageRepository.findAllByIsSentFalseOrderByStarttimeAsc()
        messages.forEach { message: Message ->
            if (message.starttime!! > LocalDateTime.now()) return true

            val messageService = MessageService()
            messageService.sendMessage(message)

            message.isSent = true
            messageRepository.save(message)
        }
        return true
    }

    /**
     * Scheduled in an hourly interval.
     * Removes all SignUp tokens that have not been used in the last hour.
     */
    @Scheduled(fixedRate = 60000)
    fun removeOldSignUpTokens() {
        val tokens = tokenRepository.findAll()
        val time = LocalDateTime.now().minusHours(1)
        tokens.forEach {
            if (it.timeLastUsed < time) {
                tokenRepository.delete(it)
            }
        }
    }
}