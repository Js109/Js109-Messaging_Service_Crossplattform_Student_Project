package de.uulm.automotive.cds.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
/**
 * Class to store a property and its respective binding in the backend.
 * Used to create the list of available properties in the creat-message view.
 */
class Property : de.uulm.automotive.cds.models.Entity() {
    @Id
    @GeneratedValue
    var id: Long? = null
    var name: String = ""

    @Column(unique = true)
    var binding: String = ""

    var isDeleted: Boolean = false
}