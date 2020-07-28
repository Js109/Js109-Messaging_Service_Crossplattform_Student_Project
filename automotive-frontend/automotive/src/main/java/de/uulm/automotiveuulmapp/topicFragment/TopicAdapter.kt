package de.uulm.automotiveuulmapp.topicFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.VolleyError
import de.uulm.automotiveuulmapp.ApplicationConstants
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.httpHandling.RestCallHelper
import de.uulm.automotiveuulmapp.topic.Callback
import de.uulm.automotiveuulmapp.topic.TopicModel
import org.json.JSONArray
import org.json.JSONObject

/**
 * Adapter that fills an RecyclerView with topic card views from a list of topics it gets from the backend.
 * This list can optionally be filtered by calling a search query.
 * @param fragment TopicFragment the RecyclerView is placed in. Needed to access preferences.
 * @param searchView SearchView whose query is used to filter the topics in the RecyclerView. Note that the firing of filter() must manually be set in the onQueryTextListener of the SearchView.
 */
class TopicAdapter(private val fragment: TopicFragment, private val searchView: SearchView, restCallHelper: RestCallHelper) :
    RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    private val topicFetcher = TopicFetcher(restCallHelper, fragment.activity?.getPreferences(Context.MODE_PRIVATE)) {notifyQueryChanged()}

    private var currentList = topicFetcher.getTopics()

    /**
     * Inflates the topic card layout to create the views in the RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val topicCard =
            LayoutInflater.from(parent.context).inflate(R.layout.topic_card, parent, false)
        return TopicViewHolder(
            topicCard
        )
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
        notifyQueryChanged()
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
     * Function to notify the adapter that the query has changed.
     * The new query will be read out of the SearchView and a new filtered list of Topics created.
     */
    fun notifyQueryChanged() {
        currentList = topicFetcher.getTopics(searchView.query.toString())
        notifyDataSetChanged()
    }

    class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int {
        return currentList.size
    }
}