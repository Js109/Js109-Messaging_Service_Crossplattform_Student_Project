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
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
class Message(
        @Id @GeneratedValue var id: Long? = null,
        var topic: String?,
        @Column(length = 127)
        var sender: String?,
        @Column(length = 127)
        var title: String?,
        @Column(length = 1023)
        var content: String?,
        var starttime: LocalDateTime?,
        var endtime: LocalDateTime?,
        var isSent: Boolean?,
        @ElementCollection(fetch = FetchType.LAZY)
        var properties: MutableList<String>?,
        @Lob
        var attachment: ByteArray?,
        @Lob
        var logoAttachment: ByteArray?,
        @ElementCollection(fetch = FetchType.LAZY)
        var links: MutableList<URL>?,
        @OneToOne(cascade = [CascadeType.ALL])
        var locationData: LocationData?,
        @OneToOne(cascade = [CascadeType.ALL])
        var messageDisplayProperties: MessageDisplayProperties?
) : de.uulm.automotive.cds.models.Entity()