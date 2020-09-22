package de.uulm.automotive.cds.repositories

import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.entities.Topic
import org.springframework.data.repository.CrudRepository

/**
 * Repository to store and query the Topics in the persistence unit.
 *
 */
interface TopicRepository : CrudRepository<Topic, Long> {
    fun findAllByOrderByTitleAsc(): Iterable<Topic>
    fun findByBinding(binding: String): Topic?
}