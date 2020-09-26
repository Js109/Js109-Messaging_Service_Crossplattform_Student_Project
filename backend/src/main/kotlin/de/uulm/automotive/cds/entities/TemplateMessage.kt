package de.uulm.automotive.cds.entities

import de.uulm.automotive.cds.models.FontFamily
import java.net.URL
import java.time.LocalDateTime
import javax.persistence.Entity

/**
 * Class for storing a template of a messages.
 * Is equal to the Message class with an additional field for the template name.
 * As Message has the @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) annotation this class will get its own table in the jpa unit.
 */
@Entity
class TemplateMessage(
        id: Long? = null,
        templateName: String?,
        topic: String?,
        sender: String?,
        title: String?,
        content: String?,
        starttime: LocalDateTime?,
        endtime: LocalDateTime?,
        isSent: Boolean?,
        properties: MutableList<String>?,
        attachment: ByteArray?,
        logoAttachment: ByteArray?,
        links: MutableList<URL>?,
        locationData: LocationData?,
        backgroundColor: String?,
        fontColor: String?,
        fontFamily: FontFamily?
) : Message(
        id,
        topic,
        sender,
        title,
        content,
        starttime,
        endtime,
        isSent,
        properties,
        attachment,
        logoAttachment,
        links,
        locationData,
        backgroundColor,
        fontColor,
        fontFamily
) {
    var templateName = templateName
}