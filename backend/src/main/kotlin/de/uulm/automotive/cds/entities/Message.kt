package de.uulm.automotive.cds.entities

import java.time.LocalDateTime
import javax.persistence.*

/**
 * Class that represents a Message that can be sent from the OEM to clients.
 *
 * @property topic Topic of the Message.
 * @property content Content of the Message.
 * @property starttime Earliest time the Message should be visible/available for the User of a Client.
 * @property endtime Latest time the Message should be visible/available for the User of a Client.
 * @property isSent Checks if Message was sent to Client.
 * @property properties
 * @property id ID of the Message.
 */
@Entity
class Message(
    var topic: String,
    var content: String,
    var starttime: LocalDateTime?,
    var endtime: LocalDateTime?,
    var isSent: Boolean?,
    @ElementCollection(fetch = FetchType.EAGER)
    var properties: MutableList<String>?,
    @Id @GeneratedValue var id: Long? = null
)