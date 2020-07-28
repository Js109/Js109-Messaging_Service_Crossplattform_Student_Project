package de.uulm.automotiveuulmapp.data

import androidx.room.TypeConverter
import java.util.*

class TypeConverters(){
    @TypeConverter
    fun fromString(s: String): UUID {
        return UUID.fromString(s)
    }

    @TypeConverter
    fun uuidToString(uuid: UUID): String{
        return uuid.toString()
    }
}