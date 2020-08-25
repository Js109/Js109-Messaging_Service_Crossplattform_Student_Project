package de.uulm.automotiveuulmapp.messages.messagedb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MessageDao {
    @Insert
    fun insert(vararg msg: MessageEntity)

    @Query("SELECT * FROM messageentity")
    fun getAll() : List<MessageEntity>

    @Query("SELECT * FROM messageentity WHERE uid = :messageId")
    fun get(messageId: Int): MessageEntity

    @Query("DELETE FROM messageentity WHERE uid = :messageId")
    fun delete(messageId: Int)

    @Update
    fun update(msg: MessageEntity)
}