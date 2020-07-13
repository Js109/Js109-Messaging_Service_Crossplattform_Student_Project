package de.uulm.automotiveuulmapp.topic

/**
 * Represents a TopicModel with its attributes
 *
 * @property id
 * @property binding
 * @property description
 * @property tags
 */
data class TopicModel(
    var id: Long,
    var title: String,
    var binding: String,
    var description: String,
    var tags: Array<String>,
    var subscribed: Boolean
)