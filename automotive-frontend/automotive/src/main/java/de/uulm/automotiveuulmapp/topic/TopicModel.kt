package de.uulm.automotiveuulmapp.topic

/**
 * Represents a TopicModel with its attributes
 *
 * @property id
 * @property binding
 * @property description
 * @property tags
 */
class TopicModel(
    var id: Long,
    var binding: String,
    var description: String,
    var tags: Array<String>
) {
}