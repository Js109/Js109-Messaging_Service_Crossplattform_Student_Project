package de.uulm.automotive.cds.repositories

import de.uulm.automotive.cds.entities.Message
import de.uulm.automotive.cds.entities.Property
import org.springframework.data.repository.CrudRepository

interface PropertyRepository : CrudRepository<Property, Long> {
    fun findAllByOrderByNameAsc(): Iterable<Property>
}