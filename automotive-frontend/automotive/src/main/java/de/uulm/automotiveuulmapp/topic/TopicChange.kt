package de.uulm.automotiveuulmapp.topic

import java.io.Serializable

/**
 * Represents a TopicChange with its attributes
 *
 * @constructor
 *
 * @param name
 * @param active
 */
class TopicChange(name: String, active: Boolean) : Serializable {
    var name = name
    var active = active
}