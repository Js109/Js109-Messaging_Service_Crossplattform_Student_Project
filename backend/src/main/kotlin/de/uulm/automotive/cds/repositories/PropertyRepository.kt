package de.uulm.automotive.cds.repositories

import de.uulm.automotive.cds.entities.Property
import org.springframework.data.repository.CrudRepository

/**
 * Repository for Properties.
 */
interface PropertyRepository : CrudRepository<Property, Long> {
    fun findAllByOrderByNameAsc(): Iterable<Property>
    fun findByBinding(binding: String): Property?
}