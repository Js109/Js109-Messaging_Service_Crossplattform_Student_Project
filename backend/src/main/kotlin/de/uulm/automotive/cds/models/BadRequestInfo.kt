package de.uulm.automotive.cds.models

/**
 * Basic Super Class for Response Objects send in the Body of a BadRequest Response
 *
 */
open class BadRequestInfo

/**
 *
 */
fun <T> T?.addErrorGeneric(addError: (err: T) -> Unit, errorInfo: T): T where T : BadRequestInfo {
    return (this ?: errorInfo)
            .apply {
                addError(this)
            }
}