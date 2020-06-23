package de.uulm.automotiveuulmapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class RegistrationData(
    @PrimaryKey
    val signUpToken: UUID,
    val queueId: UUID
)