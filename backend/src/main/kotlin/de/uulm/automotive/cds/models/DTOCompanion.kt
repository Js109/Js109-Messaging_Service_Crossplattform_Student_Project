package de.uulm.automotive.cds.models

import org.modelmapper.ModelMapper

/**
 * Basic interface for the companion object inside DTO objects
 *
 */
interface DTOCompanion {
    val mapper: ModelMapper

    fun <T : Entity> toDTO(entity: T): DTO
}