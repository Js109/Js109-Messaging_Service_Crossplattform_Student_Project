package de.uulm.automotiveuulmapp.locationFavourites.locationFavData

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class LocationData (
    @PrimaryKey
    val id: UUID,
    val name: String,
    val address: String,
    val lat: Double,
    val lng: Double
)
