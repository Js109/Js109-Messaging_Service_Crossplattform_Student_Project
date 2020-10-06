package de.uulm.automotive.cds.entities

import de.uulm.automotive.cds.models.Alignment
import de.uulm.automotive.cds.models.FontFamily
import java.io.Serializable

class MessageDisplayPropertiesSerializable (
    val backgroundColor: String?,
    val fontColor: String?,
    val fontFamily: FontFamily?,
    val alignment: Alignment?
) : Serializable