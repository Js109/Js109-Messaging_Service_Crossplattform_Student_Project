package de.uulm.automotiveuulmapp.messages.messagedb

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.uulm.automotive.cds.models.Alignment
import de.uulm.automotive.cds.models.FontFamily
import java.lang.reflect.Type
import java.net.URL

/**
 * This class is used to convert complex types to be able to store them in a rooms db
 *
 */
class Converters {
    /**
     * Deserializes a JSON-String as a list of URLs
     *
     * @param value String which should be converted back to ArrayList
     * @return ArrayList of URLs
     */
    @TypeConverter
    fun fromString(value: String?): Array<URL> {
        val listType: Type = object : TypeToken<Array<URL?>?>() {}.type
        return Gson().fromJson(value, listType)
    }


    /**
     * Serializes ArrayList of URLs as JSON to be able to store it in the db
     *
     * @param list ArrayList of URLs to serialize
     * @return Serialized JSON String
     */
    @TypeConverter
    fun fromArray(list: Array<URL?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toFontFamily(value: String?) =
        when (value) {
            null -> null
            else -> enumValueOf<FontFamily>(value)
        }

    @TypeConverter
    fun fromFontFamily(value: FontFamily?) =
        when (value) {
            null -> null
            else -> value.name
        }

    @TypeConverter
    fun toAlignment(value: String?) =
        when (value) {
            null -> null
            else -> enumValueOf<Alignment>(value)
        }

    @TypeConverter
    fun fromAlignment(value: Alignment?) =
        when (value) {
            null -> null
            else -> value.name
        }
}