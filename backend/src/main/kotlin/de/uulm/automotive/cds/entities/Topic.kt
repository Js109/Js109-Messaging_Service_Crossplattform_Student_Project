package de.uulm.automotive.cds.entities

import org.springframework.data.repository.CrudRepository
import javax.persistence.*

/**
 * Class that represents a topic that a client can subscribe to.
 * Contains the amqp binding that a client must subscribe to on a topic exchange to get messages of this topic as well as further information like a title, tags and description.
 *
 */
@Entity
class Topic : de.uulm.automotive.cds.models.Entity {
    @Id
    @GeneratedValue
    var id: Long? = null
    @Column(unique=true)
    var binding: String = ""
    var title: String = ""

    @ElementCollection
    var tags: MutableList<String> = ArrayList()
    var description: String = ""

    var isDeleted: Boolean = false

    /**
     * Validates this object
     *
     * @return true if this object is valid
     */
    override fun isValid(): Boolean {
        return validateBinding()
                && validateTitle()
                && validateDescription()
    }

    /**
     * Checks if the Binding is not blank
     *
     * @return true if the Binding is not blank
     */
    private fun validateBinding(): Boolean {
        return binding.isNotBlank()
    }

    /**
     * Checks if the Title is not blank
     *
     * @return true if the Title is not blank
     */
    private fun validateTitle(): Boolean {
        return title.isNotBlank()
    }

    /**
     * Checks if the Description is not blank
     *
     * @return true if the Description is not blank
     */
    private fun validateDescription(): Boolean {
        return description.isNotBlank()
    }
}