package de.uulm.automotiveuulmapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RegistrationData(
    @PrimaryKey val uid: Int
)