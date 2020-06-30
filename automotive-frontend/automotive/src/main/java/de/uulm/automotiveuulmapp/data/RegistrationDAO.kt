package de.uulm.automotiveuulmapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import de.uulm.automotiveuulmapp.ApplicationConstants

@Dao
interface RegistrationDAO {
    @Query("SELECT * FROM " + ApplicationConstants.REGISTRATION_DB_NAME)
    suspend fun getAll(): List<RegistrationData>
    @Query("SELECT CASE WHEN EXISTS(SELECT * FROM " + ApplicationConstants.REGISTRATION_DB_NAME + ") THEN 0 ELSE 1 END AS IsEmpty")
    suspend fun checkIfEmpty(): Boolean
    @Insert
    suspend fun insert(vararg registration: RegistrationData)
}