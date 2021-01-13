package de.uulm.automotive.cds.models

/**
 * Basic interface for Error Validation inside the DTOs
 *
 */
interface ValidateDTO {
    fun getErrors(): BadRequestInfo?

    fun hasErrors(): Boolean {
        return getErrors() != null
    }
}