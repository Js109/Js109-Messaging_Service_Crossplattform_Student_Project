package de.uulm.automotiveuulmapp.messages.messageFragment

import de.uulm.automotiveuulmapp.messages.messagedb.MessageEntity
import java.util.*

object MessageFilter {

    fun filter(messageList: List<MessageEntity>, query: String? = null) : List<MessageEntity> {
        return if (query == null || query.isEmpty()) {
            messageList
        } else {
            val (favourites, nonFavourites) = messageList.partition { it.favourite }
            filterByContent(favourites, query) + filterByContent(nonFavourites, query)
        }
    }

    private fun filterByContent(messageList: List<MessageEntity>, query: String) : List<MessageEntity> {
        val query = query.toLowerCase(Locale.ROOT)
        val (titleMatch, nonTitleMatch) = messageList.partition {
            it.title.toLowerCase(Locale.ROOT).contains(query)
        }
        val textMatch = nonTitleMatch.filter {
            it.messageText?.toLowerCase(Locale.ROOT)?.contains(query) == true
        }
        return titleMatch + textMatch
    }
}