package de.uulm.automotive.cds.repositories

import de.uulm.automotive.cds.entities.Subscriptions
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.time.LocalDate

/**
 * TODO
 *
 */
interface SubscriptionsRepository: CrudRepository<Subscriptions, Long> {
    /**
     * TODO
     *
     * @param date
     * @param topicName
     * @param propertyName
     * @return
     */
    @Query("select s from Subscriptions s where " +
            "((:topicName is null and :propertyName is null and s.topicName is null and s.propertyName is null) " +
            "or (:topicName is not null and s.topicName like :topicName) " +
            "or (:propertyName is not null and s.propertyName like :propertyName)) " +
            "and :date = s.date")
    fun findByDateFiltered(
            @Param("date") date: LocalDate,
            @Param("topicName") topicName: String? = null,
            @Param("propertyName") propertyName: String? = null
    ): Subscriptions?

    /**
     * TODO
     *
     * @param topicName
     * @param propertyName
     * @return
     */
    @Query("select s from Subscriptions s where " +
            "((:topicName is null and :propertyName is null and s.topicName is null and s.propertyName is null) " +
            "or (:topicName is not null and s.topicName like :topicName) " +
            "or (:propertyName is not null and s.propertyName like :propertyName)) " +
            "and s.date = (select max(sub.date) from Subscriptions sub)")
    fun findByMaxDateFiltered(
            @Param("topicName") topicName: String? = null,
            @Param("propertyName") propertyName: String? = null
    ): Subscriptions?

    /**
     * TODO
     *
     * @param topicName
     * @param propertyName
     * @param dateBegin
     * @param dateEnd
     * @return
     */
    @Query("select s from Subscriptions s where ((:topicName is null and :propertyName is null) " +
            "or (:topicName is not null and s.topicName like :topicName) " +
            "or (:propertyName is not null and s.propertyName like :propertyName)) " +
            "and (" +
            "(COALESCE(:dateBegin) is null or :dateBegin <= s.date) " + // postgres cannot check for null of a date object, so coalesce is used first to return null
            "and " +
            "(COALESCE(:dateEnd) is null or s.date <= :dateEnd)" +
            ")")
    fun findAllFiltered(
            @Param("topicName") topicName: String? = null,
            @Param("propertyName") propertyName: String? = null,
            @Param("dateBegin") dateBegin: LocalDate? = null,
            @Param("dateEnd") dateEnd: LocalDate? = null
    ):Iterable<Subscriptions>
}