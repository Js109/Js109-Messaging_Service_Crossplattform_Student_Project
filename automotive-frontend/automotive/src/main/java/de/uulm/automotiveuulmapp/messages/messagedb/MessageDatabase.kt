package de.uulm.automotiveuulmapp.messages.messagedb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.uulm.automotiveuulmapp.ApplicationConstants

@Database(entities = [MessageEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class MessageDatabase: RoomDatabase() {
    abstract fun messageDao(): MessageDao

    companion object{
        private var INSTANCE: MessageDatabase? = null

        /**
         * Helper method to get the same db-instance multiple times
         *
         * @param context Context
         * @return Instance of database
         */
        fun getDatabaseInstance(context: Context): MessageDatabase{
            if(INSTANCE == null){
                synchronized(MessageDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context, MessageDatabase::class.java, ApplicationConstants.MESSAGE_DB_NAME).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

}