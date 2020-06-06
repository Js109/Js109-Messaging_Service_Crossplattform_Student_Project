package de.uulm.automotive.cds

import org.springframework.data.repository.CrudRepository

interface MessageRepository : CrudRepository<Message, Long> {
    fun findAllByIsSentFalseOrderByStarttimeAsc(): Iterable<Message>
    fun findAllByOrderByTopicAsc(): Iterable<Message>
}