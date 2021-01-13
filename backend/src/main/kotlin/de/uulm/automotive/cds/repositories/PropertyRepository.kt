package de.uulm.automotive.cds.repositories

import de.uulm.automotive.cds.entities.Property
import org.springframework.data.repository.CrudRepository

/**
 * Repository for Properties.
 */
interface PropertyRepository : CrudRepository<Property, Long> {
    fun findAllByOrderByNameAscIdAsc(): Iterable<Property>
    fun findByName(name: String): Property?
    fun findAllByDisabledOrderByNameAscIdAsc(diabled: Boolean): Iterable<Property>
}