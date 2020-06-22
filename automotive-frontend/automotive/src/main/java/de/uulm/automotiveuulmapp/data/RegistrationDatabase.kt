package de.uulm.automotiveuulmapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(RegistrationData::class), version=1)
@TypeConverters(de.uulm.automotiveuulmapp.data.TypeConverters::class)
abstract class RegistrationDatabase: RoomDatabase(){
    abstract fun getRegistrationDAO(): RegistrationDAO
}