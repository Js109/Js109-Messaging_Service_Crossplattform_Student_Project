package de.uulm.automotive.cds.repositories

import de.uulm.automotive.cds.entities.Message
import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime

/**
 * Repository for Messages.
 *
 */
interface MessageRepository : CrudRepository<Message, Long> {
    fun findAllByIsSentFalseOrderByStarttimeAsc(): Iterable<Message>
    fun findAllByTitleLikeAndStarttimeBetweenAndTopic(searchString: String, dateStartTimePeriod: LocalDateTime, dateEndTimePeriod: LocalDateTime, topic: String) : Iterable<Message>
    fun findAllByStarttimeBetweenAndTopic(dateStartTimePeriod: LocalDateTime, dateEndTimePeriod: LocalDateTime, topic: String) : Iterable<Message>
    fun findAllByTitleLikeAndTopic(searchString: String, topic: String) : Iterable<Message>
    fun findAllByTitleLikeAndStarttimeBetween(searchString: String, dateStartTimePeriod: LocalDateTime, dateEndTimePeriod: LocalDateTime) : Iterable<Message>
    fun findAllByTitleLike(searchString: String) : Iterable<Message>
    fun findAllByTopic(topic: String) : Iterable<Message>
    fun findAllByStarttimeBetween(dateStartTimePeriod: LocalDateTime, dateEndTimePeriod: LocalDateTime) : Iterable<Message>
    fun findAllByOrderByTopicAsc(): Iterable<Message>
}