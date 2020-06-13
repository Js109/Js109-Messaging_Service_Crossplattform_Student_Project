package de.uulm.automotiveuulmapp.topic

import java.io.Serializable

class TopicChange(name: String, active: Boolean):Serializable {
    var name=name
    var active=active
}