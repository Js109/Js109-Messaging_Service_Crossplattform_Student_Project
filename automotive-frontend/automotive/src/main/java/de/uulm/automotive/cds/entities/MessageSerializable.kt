package de.uulm.automotive.cds.entities

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.io.Serializable
import java.net.URL

class MessageSerializable (
    var sender: String?,
    var title: String?,
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