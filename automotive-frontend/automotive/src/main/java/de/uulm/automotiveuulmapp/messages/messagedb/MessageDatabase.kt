package de.uulm.automotiveuulmapp.messages.messagedb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [MessageEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class MessageDatabase: RoomDatabase() {
    abstract fun messageDao(): MessageDao
}