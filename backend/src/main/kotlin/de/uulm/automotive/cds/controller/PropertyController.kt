package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.entities.Property
import de.uulm.automotive.cds.models.dtos.PropertyDTO
import de.uulm.automotive.cds.models.dtos.PropertyDisableDTO
import de.uulm.automotive.cds.models.errors.PropertyBadRequestInfo
import de.uulm.automotive.cds.repositories.PropertyRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

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
    fun getProperties(@RequestParam showDisabledProperties: Boolean = false): Iterable<Property> {
        if (showDisabledProperties)
            return propertyRepository.findAllByOrderByNameAscIdAsc()
        return propertyRepository.findAllByDisabledOrderByNameAscIdAsc(false)
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

        if (propertyRepository.findByName(propertyDto.name) != null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(PropertyBadRequestInfo(nameError = "Property name must be unique."))
        }

        val propertyEntity = PropertyDTO.toEntity(propertyDto)
        propertyEntity.binding = "binding/${propertyEntity.name}"

        propertyRepository.save(propertyEntity)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @PutMapping("/{id}")
    fun putPropertyEnable(@PathVariable id: Long, @RequestBody propertyDisableDto: PropertyDisableDTO) {
        val property = propertyRepository.findById(id)

        if (property.isEmpty)
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find property with id $id.")

        property.ifPresent {
            it.disabled = propertyDisableDto.disabled
            propertyRepository.save(it)
        }
    }
}