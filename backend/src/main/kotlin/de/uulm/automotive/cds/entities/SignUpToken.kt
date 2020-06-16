package de.uulm.automotive.cds.entities

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class SignUpToken(
    var signUpToken: String,
    var timeLastUsed: LocalDateTime,
    @Id @GeneratedValue var id: Long? = null
)