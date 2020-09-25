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
    fun findAllByTitleLikeIgnoreCaseOrSenderLikeIgnoreCaseOrContentLikeIgnoreCaseAndStarttimeBetweenAndTopic(searchString: String, searchStringSender: String, searchStringContent: String, dateStartTimePeriod: LocalDateTime, dateEndTimePeriod: LocalDateTime, topic: String): Iterable<Message>
    fun findAllByStarttimeBetweenAndTopic(dateStartTimePeriod: LocalDateTime, dateEndTimePeriod: LocalDateTime, topic: String): Iterable<Message>
    fun findAllByTitleLikeIgnoreCaseOrSenderLikeIgnoreCaseOrContentLikeIgnoreCaseAndTopic(searchString: String, searchStringSender: String, searchStringContent: String, topic: String): Iterable<Message>
    fun findAllByTitleLikeIgnoreCaseOrSenderLikeIgnoreCaseOrContentLikeIgnoreCaseAndStarttimeBetween(searchString: String, searchStringSender: String, searchStringContent: String, dateStartTimePeriod: LocalDateTime, dateEndTimePeriod: LocalDateTime): Iterable<Message>
    fun findAllByTopic(topic: String): Iterable<Message>
    fun findAllByStarttimeBetween(dateStartTimePeriod: LocalDateTime, dateEndTimePeriod: LocalDateTime): Iterable<Message>
    fun findAllByOrderByTopicAsc(): Iterable<Message>
    fun findAllByTitleLikeIgnoreCaseOrSenderLikeIgnoreCaseOrContentLikeIgnoreCase(searchString: String, searchStringSender: String, searchStringContent: String): Iterable<Message>
}