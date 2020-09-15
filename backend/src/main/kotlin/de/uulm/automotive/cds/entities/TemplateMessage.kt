package de.uulm.automotive.cds.entities

import java.net.URL
import java.time.LocalDateTime
import javax.persistence.*

/**
 * Class for storing a template of a messages.
 * Is equal to the Message class with an additional field for the template name.
 * As Message has the @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) annotation this class will get its own table in the jpa unit.
 */
@Entity
class TemplateMessage(id: Long? = null, templateName: String?, topic: String?, sender: String?, title: String?, content: String?, starttime: LocalDateTime?, endtime: LocalDateTime?, isSent: Boolean?, properties: MutableList<String>?, attachment: ByteArray?, links: MutableList<URL>?, locationData: LocationData?)
    : Message(id, topic, sender, title, content, starttime, endtime, isSent, properties, attachment, links, locationData) {
    var templateName = templateName
}