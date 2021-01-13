package de.uulm.automotiveuulmapp.locationFavourites.locationFavData

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import de.uulm.automotiveuulmapp.ApplicationConstants
import java.util.*

@Dao
interface LocationDataDAO {
    @Query("SELECT * FROM " + ApplicationConstants.LOCATION_DATA_DB_NAME)
    fun getAll(): LiveData<List<LocationData>>
    @Query("SELECT CASE WHEN EXISTS(SELECT * FROM " + ApplicationConstants.LOCATION_DATA_DB_NAME + ") THEN 0 ELSE 1 END AS IsEmpty")
    suspend fun checkIfEmpty(): Boolean
    @Insert
    suspend fun insert(vararg locationData: LocationData)
    @Delete
    suspend fun delete(vararg locationData: LocationData)

    @Query("SELECT * FROM " + ApplicationConstants.LOCATION_DATA_DB_NAME)
    fun getAllInstant(): List<LocationData>
}