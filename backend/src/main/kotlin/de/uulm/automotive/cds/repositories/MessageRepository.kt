package de.uulm.automotive.cds.repositories

import de.uulm.automotive.cds.entities.Message
import org.springframework.data.repository.CrudRepository

/**
 * Repository for Messages.
 *
 */
interface MessageRepository : CrudRepository<Message, Long> {
    fun findAllByIsSentFalseOrderByStarttimeAsc(): Iterable<Message>
    fun findAllByOrderByTopicAsc(): Iterable<Message>
}