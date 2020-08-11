package de.uulm.automotiveuulmapp.messages

import android.os.Parcel
import android.os.Parcelable
import de.uulm.automotiveuulmapp.messages.messagedb.MessageEntity
import java.net.URL

/**
 * This class is the same as the Message entity but required to be transferred with intents
 *
 * @property sender
 * @property title
 * @property messageText
 * @property attachment
 * @property links
 */
class ParcelableMessage(
    var messageId: Int,
    var sender: String,
    var title: String,
    var messageText: String?,
    var attachment: ByteArray?,
    var links: Array<URL>?
) : Parcelable {

    /**
     * Used to create a parcelable Message from a db entity
     */
    constructor(messageEntity: MessageEntity): this(
        messageEntity.uid!!,
        messageEntity.sender,
        messageEntity.title,
        messageEntity.messageText,
        messageEntity.attachment,
        messageEntity.links
    )

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.createByteArray(),
        parcel.createStringArray()?.map { URL(it) }?.toTypedArray()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(messageId)
        parcel.writeString(sender)
        parcel.writeString(title)
        parcel.writeString(messageText)
        parcel.writeByteArray(attachment)
        parcel.writeStringArray(links?.map { it.toString() }?.toTypedArray())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableMessage> {
        override fun createFromParcel(parcel: Parcel): ParcelableMessage {
            return ParcelableMessage(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableMessage?> {
            return arrayOfNulls(size)
        }

        fun convertToParcelableArray(msgEntities: List<MessageEntity>): ArrayList<ParcelableMessage>{
            return ArrayList( msgEntities.map { ParcelableMessage(it) })
        }
    }
}