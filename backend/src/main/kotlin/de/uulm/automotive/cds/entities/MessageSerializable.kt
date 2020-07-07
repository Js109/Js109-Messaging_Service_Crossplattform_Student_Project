package de.uulm.automotive.cds.entities

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.io.Serializable
import java.net.URL

/**
 * This object is used to transfer necessary information to the client
 * It can be serialized as byte array which is the expected format for amqp message body
 *
 * @property sender Party which created the message
 * @property title Title of the message
 * @property messageText Text content of the message
 * @property attachment Image which should be contained in the message
 * @property links Links used in the message
 */
class MessageSerializable (
        var sender: String,
        var title: String,
        var messageText: String,
        var attachment: ByteArray?,
        var links: Array<URL>?
): Serializable {
    /**
     * This converts a message object into a serialized byte array to be able to transfer it over amqp message
     *
     * @return Byte array serialized message object
     */
    fun toByteArray(): ByteArray {
        val bos = ByteArrayOutputStream()
        try {
            val out = ObjectOutputStream(bos)
            out.writeObject(this)
            out.flush()
            return bos.toByteArray()
        } finally {
            try {
                bos.close()
            } catch (ex: IOException) {
                // ignore close exception
            }
        }
    }
}