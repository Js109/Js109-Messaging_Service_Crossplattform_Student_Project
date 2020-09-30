package de.uulm.automotive.cds.models

import de.uulm.automotiveuulmapp.R

/**
 * Stores the values of the Font Families that can be used in a message in the android Frontend.
 *
 */
enum class FontFamily {
    ROBOTO, OPEN_SANS, MONTSERRAT, PLAYFAIR_DISPLAY
}

fun getFont(font: FontFamily?) =
    when (font) {
        null -> R.font.roboto
        FontFamily.ROBOTO -> R.font.roboto
        FontFamily.OPEN_SANS -> R.font.open_sans
        FontFamily.MONTSERRAT -> R.font.montserrat
        FontFamily.PLAYFAIR_DISPLAY -> R.font.playfair_display
    }