package de.uulm.automotive.cds.entities

import javax.persistence.*

/**
 * Class that represents a topic that a client can subscribe to.
 * Contains the amqp binding that a client must subscribe to on a topic exchange to get messages of this topic as well as further information like a title, tags and description.
 *
 */
@Entity
class Topic : de.uulm.automotive.cds.models.Entity() {
    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(unique = true)
    var binding: String = ""
    @Column(unique = true)
    var title: String = ""

    @ElementCollection
    var tags: MutableList<String> = ArrayList()
    var description: String = ""

    var disabled: Boolean = false
}