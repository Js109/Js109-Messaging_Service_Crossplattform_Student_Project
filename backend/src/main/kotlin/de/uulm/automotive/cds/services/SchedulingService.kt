package de.uulm.automotive.cds.services

import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.repositories.MessageRepository
import de.uulm.automotive.cds.repositories.SignUpRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

const val sendMessageIntervalInSeconds : Long = 60
const val removeTokenIntervalInSeconds : Long = 3600
const val secondToMillis: Long = 1000

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
    @Scheduled(fixedRate = sendMessageIntervalInSeconds * secondToMillis)
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
    @Scheduled(fixedRate = removeTokenIntervalInSeconds * secondToMillis)
    fun removeOldSignUpTokens() {
        val tokens = tokenRepository.findAll()
        val time = LocalDateTime.now().minusSeconds(removeTokenIntervalInSeconds)
        tokens.forEach {
            if (it.timeLastUsed < time) {
                tokenRepository.delete(it)
            }
        }
    }
}