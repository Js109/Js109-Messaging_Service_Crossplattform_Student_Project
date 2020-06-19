package de.uulm.automotiveuulmapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RegistrationDAO {
    @Query("SELECT * FROM registration")
    fun getAll(): List<RegistrationData>
    @Insert
    fun insert(vararg registration: RegistrationData)
}