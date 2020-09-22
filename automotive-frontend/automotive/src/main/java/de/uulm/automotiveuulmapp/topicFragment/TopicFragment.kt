package de.uulm.automotiveuulmapp.topicFragment

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.uulm.automotiveuulmapp.BaseFragment
import de.uulm.automotiveuulmapp.MainActivity
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.httpHandling.RestCallHelper
import de.uulm.automotiveuulmapp.rabbitmq.RabbitMQService
import de.uulm.automotiveuulmapp.topic.TopicChange

class TopicFragment : BaseFragment() {
    lateinit var mContext: Context
    private var topicAdapter: TopicAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
        if((activity as MainActivity).bound)
        topicAdapter?.messenger = (activity as MainActivity).mService
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_topic, container, false)

        val oemTopicCard = view.findViewById<View>(R.id.oemTopicCard)
        oemTopicCard.findViewById<TextView>(R.id.topicCardTitle).text =
            getString(R.string.oem_topic_card_titel)
        oemTopicCard.findViewById<TextView>(R.id.topicCardDescription).text =
            getString(R.string.oem_topic_card_description)
        val oemSwitch = oemTopicCard.findViewById<Switch>(R.id.topicCardSwitch)
        oemSwitch.isChecked = true
        oemSwitch.isEnabled = false
        oemSwitch.setOnClickListener {
            Toast.makeText(
                context,
                getString(R.string.oem_unsubsrice_impossible_message),
                Toast.LENGTH_SHORT
            ).show()
        }
        oemTopicCard.setOnClickListener { oemSwitch.callOnClick() }

        val topicSearch = view.findViewById<SearchView>(R.id.topicSearch)
        val topicAdapter = TopicAdapter(
            topicSearch,
            RestCallHelper(context),
            activity?.getPreferences(Context.MODE_PRIVATE)
        )
        val recyclerView = view.findViewById<RecyclerView>(R.id.topicsRecyclerView)
        recyclerView.apply {
            adapter = topicAdapter
            layoutManager = LinearLayoutManager(context)
        }
        topicSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                topicAdapter.notifyQueryChanged()
                oemTopicCard.isVisible = query == null || query.isEmpty()
                return true
            }

        })
        this.topicAdapter = topicAdapter

        return view
    }
}
