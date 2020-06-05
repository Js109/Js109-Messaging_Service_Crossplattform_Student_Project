package de.uulm.automotive.cds.models

import org.springframework.data.repository.CrudRepository
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Category {
    @Id
    @GeneratedValue
    var id: Long? = null
    @ElementCollection
    var bindings: MutableList<String> = ArrayList()
    @ElementCollection
    var tags: MutableList<String> = ArrayList()
    var description: String = ""

}

interface CategoryRepository: CrudRepository<Category, Long> {

}