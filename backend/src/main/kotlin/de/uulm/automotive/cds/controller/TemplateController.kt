package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.entities.TemplateMessage
import de.uulm.automotive.cds.repositories.TemplateRepository
import org.springframework.web.bind.annotation.*
import javax.transaction.Transactional

@CrossOrigin("*")
@RestController
@RequestMapping("/template")
class TemplateController(private val repository: TemplateRepository){
    @GetMapping
    @Transactional
    fun getTemplates() : Iterable<TemplateMessage> {
        return repository.findAllByOrderByTemplateNameAsc()
    }

    @PostMapping
    fun postTemplate(@RequestBody template: TemplateMessage) {
        repository.save(template)
    }

    @DeleteMapping("/{id}")
    fun deleteTemplate(@PathVariable id: Long) {
        repository.deleteById(id)
    }
}