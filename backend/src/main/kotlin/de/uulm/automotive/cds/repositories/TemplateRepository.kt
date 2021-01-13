package de.uulm.automotive.cds.repositories

import de.uulm.automotive.cds.entities.TemplateMessage
import org.springframework.data.repository.CrudRepository

interface TemplateRepository: CrudRepository<TemplateMessage, Long> {
    fun findAllByOrderByTemplateNameAsc(): Iterable<TemplateMessage>
}