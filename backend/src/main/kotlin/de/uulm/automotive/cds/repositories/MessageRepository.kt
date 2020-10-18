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
            "(:searchString = :emptyString or " +
            "(m.sender like lower('%' || :searchString || '%') or m.title like lower('%' || :searchString || '%') or m.content like lower('%' || :searchString  || '%')))" +
            "and" +
            "(:sender = :emptyString or lower(m.sender) like lower('%' || :sender || '%'))" +
            "and" +
            "(:content = :emptyString or lower(m.content) like lower('%' || :content || '%'))" +
            "and" +
            "(:title = :emptyString or lower(m.title) like lower('%' || :title || '%'))" +
            ")")
    fun findAllFiltered(
            @Param("topicName") topicName: String? = null,
            @Param("propertyName") propertyName: String? = null,
            @Param("searchString") searchString: String = "",
            @Param("sender") sender: String = "",
            @Param("content") content: String = "",
            @Param("title") title: String = "",
            @Param("dateBegin") dateBegin: LocalDateTime? = null,
            @Param("dateEnd") dateEnd: LocalDateTime? = null,
            @Param("emptyString") emptyString: String = ""
    ): Iterable<Message>

    fun findAllByIsSentFalseOrderByStarttimeAsc(): Iterable<Message>
}