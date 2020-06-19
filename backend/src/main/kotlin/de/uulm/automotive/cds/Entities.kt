package de.uulm.automotive.cds

import java.time.LocalDateTime
import javax.persistence.*

/**
 * TODO
 *
 * @property topic
 * @property content
 * @property starttime
 * @property endtime
 * @property isSent
 * @property properties
 * @property id
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