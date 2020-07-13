package de.uulm.automotive.cds.services

import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.repositories.MessageRepository
import de.uulm.automotive.cds.repositories.SignUpRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**s
 * A Service class that takes care of all actions that execute on a scheduled time.
 *
 * @property messageRepository
 * @property tokenRepository
 * @property messageService
 */
@Component
class SchedulingService(private val messageRepository: MessageRepository, private val tokenRepository: SignUpRepository, private val messageService: MessageService,
                        @Value("\${pruning.interval.seconds:60}000")
                        val removeTokenIntervalInSeconds: Long) {

    /**
     * Sends all Messages that are not sent and were scheduled to send before the current time.
     *
     * @return Boolean True if all Messages were sent
     */
    @Scheduled(fixedRateString = "\${sending.interval.seconds:60}000")
    fun sendMessages(): Boolean {
        val messages: Iterable<Message> = messageService.filterCurrentMessages()
        messages.forEach { message: Message ->
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
    @Scheduled(fixedRateString = "\${pruning.interval.seconds:3600}000")
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