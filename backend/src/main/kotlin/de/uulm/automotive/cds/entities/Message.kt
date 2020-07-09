package de.uulm.automotive.cds.entities

import org.springframework.boot.jackson.JsonObjectSerializer
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.multipart.MultipartFile
import java.net.URL
import java.time.LocalDateTime
import java.util.*
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
        @ElementCollection
        var properties: MutableList<String>?,
        var attachment: ByteArray?,
        @ElementCollection
        var links: MutableList<URL>?
)