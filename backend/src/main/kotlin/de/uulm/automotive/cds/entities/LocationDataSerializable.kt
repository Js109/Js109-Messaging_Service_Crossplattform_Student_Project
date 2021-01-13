package de.uulm.automotive.cds.entities

import java.io.Serializable

class LocationDataSerializable(val lat: Double, val lng: Double, val radius: Int) : Serializable