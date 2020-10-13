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
            "and" +
            "(:searchString is null or " +
            "(m.sender like :searchString or m.title like :searchString or m.content like :searchString))" +
            "and" +
            "(:sender is null or m.sender like :sender)" +
            "and" +
            "(:content is null or m.content like :content)" +
            "and" +
            "(:title is null or m.title like :title)" +
            ")")
    fun findAllFiltered(
            @Param("topicName") topicName: String? = null,
            @Param("propertyName") propertyName: String? = null,
            @Param("searchString") searchString: String? = null,
            @Param("sender") sender: String? = null,
            @Param("content") content: String? = null,
            @Param("title") title: String? = null,
            @Param("dateBegin") dateBegin: LocalDateTime? = null,
            @Param("dateEnd") dateEnd: LocalDateTime? = null
    ): Iterable<Message>

    fun findAllByIsSentFalseOrderByStarttimeAsc(): Iterable<Message>
}