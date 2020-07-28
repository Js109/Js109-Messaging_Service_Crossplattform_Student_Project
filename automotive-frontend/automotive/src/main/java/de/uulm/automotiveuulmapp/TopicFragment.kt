package de.uulm.automotiveuulmapp

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
import com.android.volley.Request
import com.android.volley.VolleyError
import de.uulm.automotiveuulmapp.rabbitmq.RabbitMQService
import de.uulm.automotiveuulmapp.topic.TopicChange
import de.uulm.automotiveuulmapp.topic.TopicModel
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text

class TopicFragment : BaseFragment() {
    private lateinit var mContext: Context
    private var mService: Messenger? = null
    private var bound: Boolean = false

    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            mService = Messenger(service)
            bound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null
            bound = false
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // binding service to be able to access the functions to change topic subscriptions
        Intent(mContext, RabbitMQService::class.java).also { intent ->
            (activity as Activity).bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }
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
        val topicAdapter = TopicAdapter(this, topicSearch)
        view.findViewById<RecyclerView>(R.id.topicsRecyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = topicAdapter
        }
        topicSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                topicAdapter.filter()
                oemTopicCard.isVisible = query == null || query.isEmpty()
                return true
            }

        })

        return view
    }

    /**
     * Invoking service to change topic subscriptions
     *
     * @param topicName Name of the topic of which the subscription status should be changed
     * @param topicStatus If the subscription should be enabled or disabled
     */
    fun sendTopicSubscription(topicName: String, topicStatus: Boolean) {
        mService?.send(
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
}
