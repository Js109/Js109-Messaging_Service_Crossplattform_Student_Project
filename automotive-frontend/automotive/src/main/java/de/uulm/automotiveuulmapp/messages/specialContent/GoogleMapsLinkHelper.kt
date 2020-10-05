package de.uulm.automotiveuulmapp.messages.specialContent

import java.net.URL
import java.nio.charset.StandardCharsets

class GoogleMapsLinkHelper {
    companion object{
        fun getCoordinatesFromMapsUrl(url: URL): MapsLinkInformation? {
            val pathSplit = url.path.split("/")
            val coords = pathSplit.first { it.startsWith("@") }
            val coordsSplit = coords.removeRange(0..0).split(",")
            return when(coordsSplit.size) {
                3 -> MapsLinkInformation(String(coordsSplit[0].toByteArray(), StandardCharsets.UTF_8).toDouble(), String(coordsSplit[1].toByteArray(), StandardCharsets.UTF_8).toDouble(), coordsSplit[2].removeSuffix("z").toFloat())
                2 -> MapsLinkInformation(String(coordsSplit[0].toByteArray(), StandardCharsets.UTF_8).toDouble(), String(coordsSplit[1].toByteArray(), StandardCharsets.UTF_8).toDouble(), null)
                else -> null
            }
        }
    }

    class MapsLinkInformation(val latitude:Double, val longitude: Double, val zoomLevel: Float?){ }
}