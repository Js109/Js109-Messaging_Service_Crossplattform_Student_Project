package de.uulm.automotiveuulmapp.messages.messagedb

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL

@Entity
data class MessageEntity(
    @PrimaryKey (autoGenerate = true)
    val uid: Int?,
    val sender: String,
    val title: String,
    val messageText: String?,
    val attachment: ByteArray?,
    val links: Array<URL>?,
    var favourite: Boolean = false,
    var read: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessageEntity

        if (uid != other.uid) return false
        if (sender != other.sender) return false
        if (title != other.title) return false
        if (messageText != other.messageText) return false
        if (attachment != null) {
            if (other.attachment == null) return false
            if (!attachment.contentEquals(other.attachment)) return false
        } else if (other.attachment != null) return false
        if (links != null) {
            if (other.links == null) return false
            if (!links.contentEquals(other.links)) return false
        } else if (other.links != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uid?.hashCode() ?: 0
        result = 31 * result + sender.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + messageText.hashCode()
        result = 31 * result + (attachment?.contentHashCode() ?: 0)
        result = 31 * result + (links?.contentHashCode() ?: 0)
        return result
    }

}