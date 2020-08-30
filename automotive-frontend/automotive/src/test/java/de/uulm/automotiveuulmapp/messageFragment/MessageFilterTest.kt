package de.uulm.automotiveuulmapp.messageFragment

import de.uulm.automotiveuulmapp.messages.messagedb.MessageEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class MessageFilterTest {
    @Test
    fun emptyQueryHasNoEffect() {
        val list = listOf(
            MessageEntity(1, "test", "test1", "test text 1", null, null, false),
            MessageEntity(1, "test", "test2", "test text 2", null, null, true),
            MessageEntity(1, "test", "test3", "test text 3", null, null, false)
        )
        assertThat(MessageFilter.filter(list, "")).isEqualTo(list)
    }

    @Test
    fun prioritiesFavourites() {
        val list = listOf(
            MessageEntity(1, "test", "test1", "test text 1", null, null, false),
            MessageEntity(1, "test", "test2", "test text 2", null, null, true),
            MessageEntity(1, "test", "test3", "test text 3", null, null, false)
        )
        val expectedList = listOf(
            MessageEntity(1, "test", "test2", "test text 2", null, null, true),
            MessageEntity(1, "test", "test1", "test text 1", null, null, false),
            MessageEntity(1, "test", "test3", "test text 3", null, null, false)
        )
        assertThat(MessageFilter.filter(list, "test")).isEqualTo(expectedList)
    }

    @Test
    fun titleMatchFirstThenContentMatch() {
        val list = listOf(
            MessageEntity(1, "test", "backen", "aligator", null, null, false),
            MessageEntity(1, "test", "aligator", "backen", null, null, true),
            MessageEntity(1, "test", "computer", "computer", null, null, true),
            MessageEntity(1, "test", "aligator", "backen", null, null, false),
            MessageEntity(1, "test", "computer", "computer", null, null, false),
            MessageEntity(1, "test", "backen", "aligator", null, null, true)
        )
        val expectedList = listOf(
            MessageEntity(1, "test", "aligator", "backen", null, null, true),
            MessageEntity(1, "test", "backen", "aligator", null, null, true),
            MessageEntity(1, "test", "aligator", "backen", null, null, false),
            MessageEntity(1, "test", "backen", "aligator", null, null, false)
        )
        assertThat(MessageFilter.filter(list, "aligator")).isEqualTo(expectedList)
    }

}