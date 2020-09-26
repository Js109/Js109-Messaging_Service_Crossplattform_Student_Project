package de.uulm.automotive.cds.models.errors

import de.uulm.automotive.cds.models.BadRequestInfo

/**
 * Contains the Error Information for the MessageDTO.
 *
 * @property topicError used if topic and properties are null
 * @property senderError used if sender is null
 * @property titleError used if title is null
 * @property contentError used if content and attachment are null
 * @property locationError used if location is invalid
 * @property linkError used if one or more of the links are invalid
 * @property backgroundColorError used if the string does not contain a valid hex color
 * @property fontColorError used if the string does not contain a valid hex color
 */
class MessageBadRequestInfo(
        var topicError: String? = null,
        var senderError: String? = null,
        var titleError: String? = null,
        var contentError: String? = null,
        var locationError: String? = null,
        var linkError: String? = null,
        var backgroundColorError: String? = null,
        var fontColorError: String? = null
) : BadRequestInfo()