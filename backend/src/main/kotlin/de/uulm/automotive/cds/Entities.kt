package de.uulm.automotive.cds

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Message(
        var topic: String,
        var content: String,
        var starttime: LocalDateTime?,
        var endtime: LocalDateTime?,
        var isSent: Boolean?,
        @Id @GeneratedValue var id: Long? = null
)