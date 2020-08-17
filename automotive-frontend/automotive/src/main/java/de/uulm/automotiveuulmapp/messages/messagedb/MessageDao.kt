package de.uulm.automotiveuulmapp.messages.messagedb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {
    @Insert
    fun insert(vararg msg: MessageEntity)

    @Query("SELECT * FROM messageentity")
    fun getAll() : List<MessageEntity>

    @Query("DELETE FROM messageentity WHERE uid = :messageId")
    fun delete(messageId: Int)
}