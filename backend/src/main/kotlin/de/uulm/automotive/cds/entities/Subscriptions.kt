package de.uulm.automotive.cds.entities

import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * TODO
 *
 */
@Entity
class Subscriptions (
    @Id @GeneratedValue val id: Long? = null,
    val date: LocalDate,
    val propertyName: String?,
    val topicName: String?,
    val subscribers: Int,
    val subscribersGainedLastDay: Int?
)