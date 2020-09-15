package de.uulm.automotive.cds.models

class MessageBadRequestInfo(
        var topicError: String? = null,
        var senderError: String? = null,
        var titleError: String? = null,
        var contentError: String? = null,
        var locationError: String? = null
)