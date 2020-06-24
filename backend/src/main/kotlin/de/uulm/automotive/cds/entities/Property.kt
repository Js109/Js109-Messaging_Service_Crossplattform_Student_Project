package de.uulm.automotive.cds.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
/**
 * Class to store a property and its respective binding in the backend.
 * Used to create the list of available properties in the creat-message view.
 */
class Property {
    @Id
    @GeneratedValue
    var id: Long? = null
    var name: String = ""
    var binding: String = ""
}