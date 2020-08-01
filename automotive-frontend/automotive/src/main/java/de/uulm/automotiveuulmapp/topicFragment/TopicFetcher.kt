package de.uulm.automotiveuulmapp.topicFragment

import android.content.Context
import android.content.SharedPreferences
import com.android.volley.Request
import com.android.volley.VolleyError
import de.uulm.automotiveuulmapp.ApplicationConstants
import de.uulm.automotiveuulmapp.httpHandling.RestCallHelper
import de.uulm.automotiveuulmapp.topic.Callback
import de.uulm.automotiveuulmapp.topic.TopicModel
import org.json.JSONArray
import org.json.JSONObject

/**
 * This class loads the currently available Topics in the backend and provides a method to retrieve a filtered/sorted set of those topics.
 * Topics are only fetched from the backend during creation of the object, all filter steps are performed on this set of topics.
 * To load a possibly changed set from the backend a new TopicLoader has to be created
 * Optionally can check the subscribed status of a topic stored in the SharedPreferences.
 */
class TopicFetcher(private val restCallHelper: RestCallHelper, private val preferences: SharedPreferences? = null, private val onTopicFetched: () -> Unit = { } ) {

    private val topicList = ArrayList<TopicModel>()

    init {
        loadTopics()
    }

    /**
     * Fetches the topics from the backend asynchronously and stores them in topicList.
     */
    private fun loadTopics() {
        val url = ApplicationConstants.ENDPOINT_TOPIC

        restCallHelper.callRestEndpoint(
            url,
            Request.Method.GET,
            object: Callback {
                override fun onSuccess(response: JSONObject) {
                    val jsonArray = JSONArray(response.get("array").toString())
                    for (i in 0 until jsonArray.length()) {
                        val element: JSONObject = jsonArray.optJSONObject(i)
                        val tags: ArrayList<String> = ArrayList()
                        for (tag in 0 until element.getJSONArray("tags").length()) {
                            tags.add(element.getJSONArray("tags").get(tag) as String)
                        }
                        val topic = TopicModel(
                            element.getLong("id"),
                            element.getString("title"),
                            element.getString("binding"),
                            element.getString("description"),
                            tags.toTypedArray(),
                            false
                        )
                        topic.subscribed = loadTopicIsSubscribed(topic)
                        topicList.add(topic)
                    }
                    onTopicFetched()
                }

                override fun onFailure(volleyError: VolleyError) {
//                    TODO("Not yet implemented")
                }
            })
    }

    /**
     * Retrieves the subscription status of a topic from the Preferences.
     */
    private fun loadTopicIsSubscribed(topic: TopicModel): Boolean {
        return preferences?.getBoolean("topic/${topic.id}", false) ?: false
    }

    /**
     *
     */
    fun getTopics(filter: String = ""): List<TopicModel> {
        return TopicFilter.filter(topicList, filter)
    }
}