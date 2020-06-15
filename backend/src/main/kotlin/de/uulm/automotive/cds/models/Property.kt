package de.uulm.automotive.cds.models

import org.springframework.data.repository.CrudRepository
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Property {
    @Id
    @GeneratedValue
    var id: Long? = null
    var binding: String = ""
}

interface PropertyRepository: CrudRepository<Property, Long> {

}