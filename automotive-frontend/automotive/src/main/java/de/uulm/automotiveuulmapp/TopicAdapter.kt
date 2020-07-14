package de.uulm.automotiveuulmapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Switch
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.VolleyError
import de.uulm.automotiveuulmapp.topic.TopicModel
import org.json.JSONArray
import org.json.JSONObject

/**
 * Adapter that fills an RecyclerView with topic card views from a list of topics it gets from the backend.
 * This list can optionally be filtered by calling a search query.
 * @param fragment TopicFragment the RecyclerView is placed in. Needed to access preferences.
 * @param searchView SearchView whose query is used to filter the topics in the RecyclerView. Note that the firing of filter() must manually be set in the onQueryTextListener of the SearchView.
 */
class TopicAdapter(private val fragment: TopicFragment, private val searchView: SearchView) :
    RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    init {
        loadTopics()
    }

    /**
     * List containing all currently available topics in the backend
     */
    private var topicList: MutableList<TopicModel> = ArrayList()

    /**
     * List containing all topics that should be currently displayed by the RecyclerView
     */
    private var currentList: List<TopicModel> = emptyList()

    /**
     * Fetches the topics from the backend asynchronously and stores them in topicList.
     * After all topics are read from the json response they are copied to currentList and notifyDataSetChanged() is invoked.
     */
    private fun loadTopics() {
        val url = ApplicationConstants.ENDPOINT_TOPIC

        (fragment.activity as SubscribeActivity).callRestEndpoint(
            url,
            Request.Method.GET,
            { response: JSONObject ->
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
                filter()
            },
            { error: VolleyError ->
                Log.e("Topic", "Failed to load topics")
            })
    }

    /**
     * Retrieves the subscription status of a topic from the Preferences.
     */
    private fun loadTopicIsSubscribed(topic: TopicModel): Boolean {
        return fragment.activity?.getPreferences(Context.MODE_PRIVATE)
            ?.getBoolean("topic/${topic.id}", false) ?: false
    }

    /**
     * Inflates the topic card layout to create the views in the RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val topicCard =
            LayoutInflater.from(parent.context).inflate(R.layout.topic_card, parent, false)
        return TopicViewHolder(topicCard)
    }

    /**
     * Fills a topic card view with the values of a topic in the current data set.
     */
    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val title = holder.itemView.findViewById<TextView>(R.id.topicCardTitle)
        title.text = currentList[position].title

        val description = holder.itemView.findViewById<TextView>(R.id.topicCardDescription)
        description.text = currentList[position].description

        val switch = holder.itemView.findViewById<Switch>(R.id.topicCardSwitch)
        switch.setOnCheckedChangeListener { _, _ -> }
        switch.isChecked = currentList[position].subscribed
        switch.setOnCheckedChangeListener { _, isChecked ->
            topicCardSwitchChange(currentList[position], isChecked)
        }

        holder.itemView.setOnClickListener {
            switch.isChecked = !switch.isChecked
        }
    }

    /**
     * Handles the change of the subscription switch for a topic.
     */
    private fun topicCardSwitchChange(topic: TopicModel, isChecked: Boolean) {
        topic.subscribed = isChecked
        storeTopicIsSubscribed(topic, isChecked)
        fragment.sendTopicSubscription(topic.binding, isChecked)
        filter()
    }

    /**
     * Stores the subscribed status of a topic via Preferences.
     * @param topic Topic for which the subsriced status should be stored
     * @param subscribed Whether the subscription is active or not
     */
    private fun storeTopicIsSubscribed(topic: TopicModel, subscribed: Boolean) {
        fragment.activity?.getPreferences(Context.MODE_PRIVATE)?.edit()?.apply {
            putBoolean("topic/${topic.id}", subscribed)
            apply()
        }
    }

    /**
     * Filters the topics displayed by the RecyclerView by the query currently in the search bar.
     */
    fun filter() {
        val query = searchView.query
        currentList = if (query == null || query.isEmpty()) {
            topicList.sortedBy { !it.subscribed }
        } else {
            filteredTopicList(topicList, query.toString())
        }
        notifyDataSetChanged()
    }

    /**
     * Creates a filtered list of the given topics by a given query String.
     * Only the topic containing the query in the title, description or tags will be returned, in that order
     * @param topicList list of TopicModels to be filtered
     * @param query String that the topics will be filtered by
     * @return List of Topics containing first those that matched by title then those matched by description then those matched by tags.
     */
    private fun filteredTopicList(topicList: List<TopicModel>, query: String): List<TopicModel> {
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

    class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int {
        return currentList.size
    }
}