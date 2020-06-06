package de.uulm.automotive.cds.models

data class Message(
        val categoryIds: List<Long>,
        val content: String
)