package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.entities.Property
import de.uulm.automotive.cds.repositories.PropertyRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/property")
class PropertyController (val propertyRepository: PropertyRepository) {
    @GetMapping()
    fun getProperties(): Iterable<Property> {
        return propertyRepository.findAll()
    }

    @PostMapping()
    fun postProperty(@RequestBody property: Property) {
        propertyRepository.save(property)
    }
}