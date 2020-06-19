package de.uulm.automotiveuulmapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(RegistrationData::class), version=1)
abstract class Database: RoomDatabase(){
    abstract fun registrationDAO(): RegistrationDAO
}