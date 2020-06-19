package de.uulm.automotive.cds

import org.springframework.data.repository.CrudRepository

/**
 * TODO
 *
 */
interface MessageRepository : CrudRepository<Message, Long> {
    fun findAllByIsSentFalseOrderByStarttimeAsc(): Iterable<Message>
    fun findAllByOrderByTopicAsc(): Iterable<Message>
}