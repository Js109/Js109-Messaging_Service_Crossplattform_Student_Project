package de.uulm.automotive.cds.models

import org.springframework.data.repository.CrudRepository
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * Class that represents a topic that a client can subscribe to.
 * Contains the amqp binding that a client must subscribe to on a topic exchange to get messages of this topic as well as further information like a title, tags and description.
 *
 */
@Entity
class Topic {
    @Id
    @GeneratedValue
    var id: Long? = null
    var binding: String = ""
    var title: String = ""

    @ElementCollection
    var tags: MutableList<String> = ArrayList()
    var description: String = ""
}

/**
 * Repository to store and query the Topics in the persistence unit.
 *
 */
interface TopicRepository : CrudRepository<Topic, Long> {

}