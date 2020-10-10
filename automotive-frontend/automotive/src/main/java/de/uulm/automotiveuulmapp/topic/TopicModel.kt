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
    var subscribed: Boolean,
    var disabled: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TopicModel

        if (id != other.id) return false
        if (title != other.title) return false
        if (binding != other.binding) return false
        if (description != other.description) return false
        if (!tags.contentEquals(other.tags)) return false
        if (subscribed != other.subscribed) return false
        if (disabled != other.disabled) return false

        return true
    }
}