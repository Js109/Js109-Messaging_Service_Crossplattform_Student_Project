package de.uulm.automotive.cds.repositories

import de.uulm.automotive.cds.entities.Message
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

/**
 * Repository for Messages.
 *
 */
interface MessageRepository : CrudRepository<Message, Long> {
    @Query("select m from Message m where ((:topicName is null and :propertyName is null) " +
            "or (:topicName is not null and m.topic like :topicName) " +
            "or (:propertyName is not null and :propertyName member of m.properties)) " +
            "and (" +
            "(COALESCE(:dateBegin) is null or :dateBegin <= m.starttime) " + // postgres cannot check for null of a date object, so coalesce is used first to return null
            "and " +
            "(COALESCE(:dateEnd) is null or m.starttime <= :dateEnd)" +
            ")")
    fun findAllFiltered(
            @Param("topicName") topicName: String?,
            @Param("propertyName") propertyName: String?,
            @Param("dateBegin") dateBegin: LocalDateTime? = null,
            @Param("dateEnd") dateEnd: LocalDateTime? = null
    ): Iterable<Message>

    fun findAllByIsSentFalseOrderByStarttimeAsc(): Iterable<Message>
    fun findAllByTitleLikeIgnoreCaseOrSenderLikeIgnoreCaseOrContentLikeIgnoreCaseAndStarttimeBetweenAndTopic(searchString: String, searchStringSender: String, searchStringContent: String, dateStartTimePeriod: LocalDateTime, dateEndTimePeriod: LocalDateTime, topic: String): Iterable<Message>
    fun findAllByStarttimeBetweenAndTopic(dateStartTimePeriod: LocalDateTime, dateEndTimePeriod: LocalDateTime, topic: String): Iterable<Message>
    fun findAllByTitleLikeIgnoreCaseOrSenderLikeIgnoreCaseOrContentLikeIgnoreCaseAndTopic(searchString: String, searchStringSender: String, searchStringContent: String, topic: String): Iterable<Message>
    fun findAllByTitleLikeIgnoreCaseOrSenderLikeIgnoreCaseOrContentLikeIgnoreCaseAndStarttimeBetween(searchString: String, searchStringSender: String, searchStringContent: String, dateStartTimePeriod: LocalDateTime, dateEndTimePeriod: LocalDateTime): Iterable<Message>
    fun findAllByTopic(topic: String): Iterable<Message>
    fun findAllByStarttimeBetween(dateStartTimePeriod: LocalDateTime, dateEndTimePeriod: LocalDateTime): Iterable<Message>
    fun findAllByTitleLikeIgnoreCaseOrSenderLikeIgnoreCaseOrContentLikeIgnoreCase(searchString: String, searchStringSender: String, searchStringContent: String): Iterable<Message>
}