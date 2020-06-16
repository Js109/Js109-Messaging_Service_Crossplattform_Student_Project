package de.uulm.automotive.cds.models

import org.springframework.data.repository.CrudRepository
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Topic {
    @Id
    @GeneratedValue
    var id: Long? = null
    var binding: String = ""
    @ElementCollection
    var tags: MutableList<String> = ArrayList()
    var description: String = ""
}

interface TopicRepository: CrudRepository<Topic, Long> {

}