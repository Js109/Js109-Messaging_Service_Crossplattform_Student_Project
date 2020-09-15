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
class Property : de.uulm.automotive.cds.models.Entity {
    @Id
    @GeneratedValue
    var id: Long? = null
    var name: String = ""
    @Column(unique=true)
    var binding: String = ""

    var isDeleted: Boolean = false

    /**
     * Validates this object
     *
     * @return true if this object is valid
     */
    override fun isValid(): Boolean {
        return validateName()
                && validateBinding()
    }

    /**
     * Checks if the Name is not blank
     *
     * @return true if the Binding is not blank
     */
    private fun validateName() : Boolean {
        return name.isNotBlank()
    }

    /**
     * Checks if the Binding is not blank
     *
     * @return true if the Binding is not blank
     */
    private fun validateBinding() : Boolean {
        return binding.isNotBlank()
    }
}