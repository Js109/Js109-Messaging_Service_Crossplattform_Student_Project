package de.uulm.automotiveuulmapp.messages.messagedb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.uulm.automotiveuulmapp.ApplicationConstants

@Database(entities = [MessageEntity::class], version = 3)
@TypeConverters(Converters::class)
abstract class MessageDatabase: RoomDatabase() {
    abstract fun messageDao(): MessageDao

    companion object{
        private var DB_INSTANCE: MessageDatabase? = null
        private var DAO_INSTANCE: MessageDao? = null

        /**
         * Helper method to get the same db-instance multiple times
         *
         * @param context Context
         * @return Instance of database
         */
        fun getDatabaseInstance(context: Context): MessageDatabase{
            if(DB_INSTANCE == null){
                synchronized(MessageDatabase::class.java) {
                    if (DB_INSTANCE == null) {
                        DB_INSTANCE = Room.databaseBuilder(context, MessageDatabase::class.java, ApplicationConstants.MESSAGE_DB_NAME).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return DB_INSTANCE!!
        }

        fun getDaoInstance(context: Context): MessageDao {
            if(DAO_INSTANCE == null) {
                synchronized(MessageDatabase::class.java) {
                    DAO_INSTANCE = getDatabaseInstance(context).messageDao()
                }
            }
            return DAO_INSTANCE!!
        }
    }

}