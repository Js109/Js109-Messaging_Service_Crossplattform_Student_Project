package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.models.Property
import de.uulm.automotive.cds.models.PropertyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PropertyControler @Autowired constructor(private val propertyRepository: PropertyRepository){
    @GetMapping("/property")
    fun getProperty(): Iterable<Property> {
        return propertyRepository.findAll()
    }
}