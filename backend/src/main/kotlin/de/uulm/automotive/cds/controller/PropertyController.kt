package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.entities.Property
import de.uulm.automotive.cds.models.dtos.PropertyDTO
import de.uulm.automotive.cds.models.errors.PropertyBadRequestInfo
import de.uulm.automotive.cds.repositories.PropertyRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin("*")
@RestController
@RequestMapping("/property")
/**
 * REST-Endpoint for storing and reading properties.
 */
class PropertyController(val propertyRepository: PropertyRepository) {

    /**
     * REST-Endpoint to get all available properties in the system.
     * See swagger definition of GET /property for more details.
     *
     * @return
     */
    @GetMapping
    fun getProperties(): Iterable<Property> {
        return propertyRepository.findAll()
    }

    /**
     * REST-Endpoint for storing a new property.
     * See swagger definition of POST /property for more details.
     *
     * @param propertyDto DTO of the property
     * @return Response Entity Containing Error Object in case of an invalid DTO
     */
    @PostMapping
    fun postProperty(@RequestBody propertyDto: PropertyDTO): ResponseEntity<PropertyBadRequestInfo> {
        val errors = propertyDto.getErrors()
        if (errors != null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors)
        }

        propertyRepository.save(propertyDto.toEntity())
        return ResponseEntity.status(HttpStatus.OK).build()
    }
}