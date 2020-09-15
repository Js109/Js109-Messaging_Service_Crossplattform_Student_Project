package de.uulm.automotive.cds.models

/**
 * Basic Interface for DTO objects
 *
 */
interface DTO {
    fun toEntity() : Entity
    fun isValid() : Boolean
}