package de.uulm.automotiveuulmapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RegistrationDAO {
    @Query("SELECT * FROM registrationData")
    suspend fun getAll(): List<RegistrationData>
    @Query("SELECT CASE WHEN EXISTS(SELECT * FROM registrationData) THEN 0 ELSE 1 END AS IsEmpty")
    suspend fun checkIfEmpty(): Boolean
    @Insert
    suspend fun insert(vararg registration: RegistrationData)
}