package de.uulm.automotive.cds.models

class MessageDisplayProperties(
    val fontColor: String?,
    val backgroundColor: String?,
    val fontFamily: FontFamily?,
    val alignment: Alignment?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true

        if (javaClass != other?.javaClass)
            return false

        other as MessageDisplayProperties

        if (fontColor != other.fontColor)
            return false
        if (backgroundColor != other.backgroundColor)
            return false
        if (fontFamily != other.fontFamily)
            return false
        if (alignment != other.alignment)
            return false

        return true
    }

    override fun hashCode(): Int {
        var result = fontColor.hashCode()
        result = 31 * result + backgroundColor.hashCode()
        result = 31 * result + fontFamily.hashCode()
        result = 31 * result + alignment.hashCode()
        return result
    }
}