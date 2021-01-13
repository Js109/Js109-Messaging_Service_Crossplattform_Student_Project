package de.uulm.automotiveuulmapp.topicFragment

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.httpHandling.RestCallHelper
import de.uulm.automotiveuulmapp.rabbitmq.RabbitMQService
import de.uulm.automotiveuulmapp.topic.TopicChange
import de.uulm.automotiveuulmapp.topic.TopicModel

/**
 * Adapter that fills an RecyclerView with topic card views from a list of topics it gets from the backend.
 * This list can optionally be filtered by calling a search query.s.
 * @param searchView SearchView whose query is used to filter the topics in the RecyclerView. Note that the firing of filter() must manually be set in the onQueryTextListener of the SearchView.
 * @param restCallHelper RestCallHelper used to fetch the topics from the backend
 * @param preferences optional Preferences, that allow the Adapter to load the subscribed property of the topics
 */
class TopicAdapter(
    private val searchView: SearchView,
    restCallHelper: RestCallHelper,
    private val preferences: SharedPreferences? = null
) :
    RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    private var currentList: MutableList<TopicModel> = emptyList<TopicModel>().toMutableList()
    private lateinit var context: Context

    private val topicFetcher = TopicFetcher(restCallHelper, preferences) { notifyQueryChanged() }

    var messenger: Messenger? = null

    /**
     * Inflates the topic card layout to create the views in the RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        this.context = parent.context
        val topicCard =
            LayoutInflater.from(context).inflate(R.layout.topic_card, parent, false)
        return TopicViewHolder(
            topicCard
        )
    }

    /**
     * Fills a topic card view with the values of a topic in the current data set.
     */
    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val selectedTopic = currentList[position]
        val title = holder.itemView.findViewById<TextView>(R.id.topicCardTitle)
        title.text = selectedTopic.title

        val description = holder.itemView.findViewById<TextView>(R.id.topicCardDescription)
        description.text = selectedTopic.description

        val switch = holder.itemView.findViewById<Switch>(R.id.topicCardSwitch)
        switch.setOnCheckedChangeListener { _, _ -> }
        switch.isChecked = selectedTopic.subscribed
        switch.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                false -> handleUnsubscribe(selectedTopic, switch, position)
                true -> topicCardSwitchChange(currentList[position], isChecked)
            }
        }

        holder.itemView.setOnClickListener {
            switch.isChecked = !switch.isChecked
        }
    }

    /**
     * Handles the unsubscribe envent.
     * If the passed topic is disabled, triggers a confirmation dialog. If confirmed, unsubscribes
     * and removes the topic from the list.
     *
     * @param selectedTopic Topic which the unsubscribe was called on
     * @param switch Required to reset switch to its original state when aborted
     * @param position Position of the element in the list to be able to remove it
     */
    private fun handleUnsubscribe(selectedTopic: TopicModel, switch: Switch, position: Int){
        if(selectedTopic.disabled) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(R.string.unsubscribe_dialog_content)
                .setNegativeButton(R.string.dialog_confirmation_no
                ) { _, _ ->
                    switch.toggle()
                }
                .setPositiveButton(R.string.dialog_confirmation_yes
                ) { _, _ ->
                    currentList.removeAt(position)
                    notifyItemRemoved(position)
                }
            builder.create().show()
        }

        topicCardSwitchChange(currentList[position], false)
    }

    /**
     * Handles the change of the subscription switch for a topic.
     */
    private fun topicCardSwitchChange(topic: TopicModel, isChecked: Boolean) {
        topic.subscribed = isChecked
        storeTopicIsSubscribed(topic, isChecked)
        sendTopicSubscription(topic.binding, isChecked)
        notifyQueryChanged()
    }

    /**
     * Invoking service to change topic subscriptions
     *
     * @param topicName Name of the topic of which the subscription status should be changed
     * @param topicStatus If the subscription should be enabled or disabled
     */
    private fun sendTopicSubscription(topicName: String, topicStatus: Boolean) {
        messenger?.send(
            Message.obtain(
                null,
                RabbitMQService.MSG_CHANGE_TOPICS,
                0,
                0,
                TopicChange(topicName, topicStatus)
            )
        )
        if (topicStatus) {
            Log.d("Topic", "Subscribing to topic" + topicName)
        } else {
            Log.d("Topic", "Unsubscribing from topic" + topicName)
        }
    }

    /**
     * Stores the subscribed status of a topic via Preferences.
     * @param topic Topic for which the subsriced status should be stored
     * @param subscribed Whether the subscription is active or not
     */
    private fun storeTopicIsSubscribed(topic: TopicModel, subscribed: Boolean) {
        preferences?.edit()?.apply {
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