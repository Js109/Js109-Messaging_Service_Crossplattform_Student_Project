package de.uulm.automotive.cds.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class LocationData (@Id @GeneratedValue var id: Long?, var lat: Double, var lng: Double, var radius: Int) {
    fun serialize(): LocationDataSerializable {
        return LocationDataSerializable(lat, lng, radius)
    }
}