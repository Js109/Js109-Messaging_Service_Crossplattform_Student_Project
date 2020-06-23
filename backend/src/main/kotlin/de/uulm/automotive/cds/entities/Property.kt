package de.uulm.automotive.cds.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Property {
    @Id
    @GeneratedValue
    var id: Long? = null
    var name: String = ""
    var binding: String = ""
}