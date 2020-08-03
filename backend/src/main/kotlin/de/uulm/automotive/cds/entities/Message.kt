package de.uulm.automotive.cds.entities

import java.net.URL
import java.time.LocalDateTime
import javax.persistence.*

/**
 * Class that represents a Message that can be sent from the OEM to clients.
 *
 * @property id ID of the Message.
 * @property topic Topic of the message.
 * @property sender Creator and sender of the message
 * @property title Title or heading of the message
 * @property content Content of the message.
 * @property starttime Earliest time the message should be visible/available for the user of a client.
 * @property endtime Latest time the message should be visible/available for the user of a client.
 * @property isSent Checks if message was sent to client.
 * @property properties Properties of the recipient device which should be addressed
 * @property attachment Image which should be displayed within the message
 * @property links Links used in the message
 */
@Entity
class Message(
        @Id @GeneratedValue var id: Long? = null,
        var topic: String?,
        var sender: String?,
        var title: String?,
        var content: String,
        var starttime: LocalDateTime?,
        var endtime: LocalDateTime?,
        var isSent: Boolean?,
        @ElementCollection(fetch = FetchType.LAZY)
        var properties: MutableList<String>?,
        @Lob
        var attachment: ByteArray?,
        @ElementCollection(fetch = FetchType.LAZY)
        var links: MutableList<URL>?,
        lat: Double?,
        lng: Double?,
        radius: Int?
) {
        @OneToOne(cascade = [CascadeType.ALL])
        val locationData: LocationData? = if(lat != null && lng != null && radius != null) LocationData(null,lat, lng, radius) else null
}