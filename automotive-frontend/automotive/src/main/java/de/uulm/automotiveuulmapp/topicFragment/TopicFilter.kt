package de.uulm.automotiveuulmapp.topicFragment

import de.uulm.automotiveuulmapp.topic.TopicModel

object TopicFilter {
    /**
     * Creates a filtered list of the given topics by a given query String.
     * Only the topic containing the query in the title, description or tags will be returned, in that order.
     * If no/ an empty query is passed subscribed Topics will be moved to the front.
     * @param topicList list of TopicModels to be filtered
     * @param query String that the topics will be filtered by
     * @return List of Topics containing first those that matched by title then those matched by description then those matched by tags.
     */
    fun filter(topicList: MutableList<TopicModel>, query: String? = null) : List<TopicModel> {
        return if (query == null || query.isEmpty()) {
            topicList.sortedBy { !it.subscribed }
        } else {
            val query = query.toLowerCase()
            val (titleMatch, nonTitleMatch) = topicList.partition {
                it.title.toLowerCase().contains(query)
            }
            val (descriptionMatch, nonDescriptionMatch) = nonTitleMatch.partition {
                it.description.toLowerCase().contains(query)
            }
            val tagMatch = nonDescriptionMatch.filter {
                it.tags.any { tag ->
                    tag.toLowerCase().contains(query)
                }
            }
            return titleMatch.sortedBy { it.title } + descriptionMatch.sortedBy { it.title } + tagMatch.sortedBy { it.title }
        }
    }
}