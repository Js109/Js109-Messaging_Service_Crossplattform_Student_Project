package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.entities.TemplateMessage
import de.uulm.automotive.cds.repositories.TemplateRepository
import org.springframework.web.bind.annotation.*
import javax.transaction.Transactional

/**
 * REST-Resource to store and retrieve messages for later reuse/sending (templates).
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/template")
class TemplateController(private val repository: TemplateRepository){

    /**
     * REST-Endpoint to Retrieve all templates stored in the system.
     * @return  List of templates ordered by name
     */
    @GetMapping
    @Transactional
    fun getTemplates() : Iterable<TemplateMessage> {
        return repository.findAllByOrderByTemplateNameAsc()
    }

    /**
     * REST-Endpoint to store a new template
     *
     * @param template Template object to be stored in the system
     */
    @PostMapping
    fun postTemplate(@RequestBody template: TemplateMessage) {
        template.id = null
        template.messageDisplayProperties?.id = null
        repository.save(template)
    }

    /**
     * REST-Endpoint to delete a template
     *
     * @param id Id of the template that is to be deleted
     */
    @DeleteMapping("/{id}")
    fun deleteTemplate(@PathVariable id: Long) {
        repository.deleteById(id)
    }
}