package de.uulm.automotiveuulmapp.messageFragment

import de.uulm.automotiveuulmapp.messages.messagedb.MessageEntity
import de.uulm.automotiveuulmapp.topic.TopicModel

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
        val query = query.toLowerCase()
        val (titleMatch, nonTitleMatch) = messageList.partition {
            it.title.toLowerCase().contains(query)
        }
        val textMatch = nonTitleMatch.filter {
            it.messageText.toLowerCase().contains(query)
        }
        return titleMatch + textMatch
    }
}