package de.uulm.automotiveuulmapp.locationFavourites.locationFavData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.uulm.automotiveuulmapp.ApplicationConstants


@Database(entities = [LocationData::class], version=1)
@TypeConverters(de.uulm.automotiveuulmapp.data.TypeConverters::class)
abstract class LocationDatabase: RoomDatabase(){

    abstract fun getLocationDao(): LocationDataDAO

    companion object{
        private var INSTANCE: LocationDatabase? = null

        /**
         * Helper method to get the same db-instance multiple times
         *
         * @param context Context
         * @return Instance of database
         */
        fun getDatabaseInstance(context: Context): LocationDatabase{
            if(INSTANCE == null){
                synchronized(LocationDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context, LocationDatabase::class.java, ApplicationConstants.LOCATION_DATA_DB_NAME).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}