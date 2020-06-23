package de.uulm.automotive.cds.services

import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.repositories.MessageRepository
import de.uulm.automotive.cds.repositories.SignUpRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.temporal.TemporalUnit

const val minute : Long = 60
const val hour : Long = 3600

/**s
 * A Service class that takes care of all actions that execute on a scheduled time.
 *
 * @property messageRepository
 * @property tokenRepository
 * @property messageService
 */
@Component
class SchedulingService(private val messageRepository: MessageRepository, private val tokenRepository: SignUpRepository, private val messageService: MessageService) {

    /**
     * Sends all Messages that are not sent and were scheduled to send before the current time.
     *
     * @return Boolean True if all Messages were sent
     */
    @Scheduled(fixedRate = minute * 1000)
    fun sendMessages(): Boolean {
        val messages = messageRepository.findAllByIsSentFalseOrderByStarttimeAsc()
        messages.forEach { message: Message ->
            if (message.starttime!! > LocalDateTime.now()) return true

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
    @Scheduled(fixedRate = hour * 1000)
    fun removeOldSignUpTokens() {
        val tokens = tokenRepository.findAll()
        val time = LocalDateTime.now().minusSeconds(hour)
        tokens.forEach {
            if (it.timeLastUsed < time) {
                tokenRepository.delete(it)
            }
        }
    }
}