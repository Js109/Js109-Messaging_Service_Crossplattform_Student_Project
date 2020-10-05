package de.uulm.automotive.cds.entities

import de.uulm.automotive.cds.models.Alignment
import de.uulm.automotive.cds.models.FontFamily
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class MessageDisplayProperties(
        @Id @GeneratedValue var id: Long? = null,
        @Column(length = 7)
        var backgroundColor: String?,
        @Column(length = 7)
        var fontColor: String?,
        var fontFamily: FontFamily?,
        var alignment: Alignment?
) : de.uulm.automotive.cds.models.Entity() {
    fun serialize() : MessageDisplayPropertiesSerializable =
            MessageDisplayPropertiesSerializable(
                    backgroundColor,
                    fontColor,
                    fontFamily,
                    alignment
            )
}