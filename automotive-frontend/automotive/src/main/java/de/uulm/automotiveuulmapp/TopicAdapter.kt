package de.uulm.automotiveuulmapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.uulm.automotiveuulmapp.topic.TopicModel

class TopicAdapter(private val topicList: List<TopicModel>): RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    private var currentList: List<TopicModel> = topicList.filter { _ -> true }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val topicCard = LayoutInflater.from(parent.context).inflate(R.layout.topic_card, parent, false)
        return TopicViewHolder(topicCard)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val switch = holder.itemView.findViewById<Switch>(R.id.topicCardSwitch)
        switch.isChecked = currentList[position].subscribed
        switch.setOnCheckedChangeListener{buttonView, isChecked ->
            currentList[position].subscribed = isChecked
        }

        val title = holder.itemView.findViewById<TextView>(R.id.topicCardTitle)
        title.text = currentList[position].title

        val description = holder.itemView.findViewById<TextView>(R.id.topicCardDescription)
        description.text = currentList[position].description
    }

    fun filter(query: String?) {
        currentList = if (query == null || query.isEmpty()) {
            topicList.toList()
        } else {
            topicList.filterIndexed { index, _ -> index % 2 == 0 }
        }
        notifyDataSetChanged()
    }

    class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun getItemCount(): Int {
        return currentList.size
    }

}