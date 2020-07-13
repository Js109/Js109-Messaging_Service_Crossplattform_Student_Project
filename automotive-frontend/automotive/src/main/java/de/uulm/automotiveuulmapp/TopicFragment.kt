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
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.VolleyError
import de.uulm.automotiveuulmapp.rabbitmq.RabbitMQService
import de.uulm.automotiveuulmapp.topic.TopicChange
import de.uulm.automotiveuulmapp.topic.TopicModel
import org.json.JSONArray
import org.json.JSONObject

class TopicFragment : BaseFragment() {
    private lateinit var mContext: Context
    private var mService: Messenger? = null
    private var bound: Boolean = false
    private val topicList: MutableList<TopicModel> = ArrayList()
    private var topicAdapter = TopicAdapter(topicList)

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
        val v = inflater.inflate(R.layout.fragment_topic, container, false)
        

        loadAvailableTopics(v)
        return v
    }

    /**
     * Requests all available topics from the backend api
     *
     * @param view The view where the topic listing should be added to
     */
    private fun loadAvailableTopics(view: View){
        val url = ApplicationConstants.ENDPOINT_TOPIC

        (activity as SubscribeActivity).callRestEndpoint(url, Request.Method.GET, { response: JSONObject ->
            val jsonArray = JSONArray(response.get("array").toString())
            for (i in 0 until jsonArray.length()){
                val element: JSONObject = jsonArray.optJSONObject(i)
                val tags:ArrayList<String> = ArrayList()
                for(tag in 0 until element.getJSONArray("tags").length()){
                    tags.add(element.getJSONArray("tags").get(i) as String)
                }
                val topic = TopicModel(
                    element.getLong("id"),
                    element.getString("title"),
                    element.getString("binding"),
                    element.getString("description"),
                    tags.toTypedArray(),
                    false)
                topic.subscribed = activity?.getPreferences(Context.MODE_PRIVATE)?.getBoolean("topic/${topic.id}", false) ?: false
                topicList.add(topic)
            }
            topicAdapter = TopicAdapter(topicList)
            val topicRecyclerView = view.findViewById<RecyclerView>(R.id.topicsRecyclerView).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = topicAdapter
            }
            val topicSearch = view.findViewById<SearchView>(R.id.topicSearch).setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    topicAdapter.filter(query)
                    return true
                }

            })
        }, { error: VolleyError ->
            Log.e("Topic","Failed to load topics")
        })
    }

    /**
     * Adding switches to the view to change subscriptions of the device.
     *
     * @param view View to which the
     * @param topicArrayList List of topics available to subscribe to
     */
    private fun addTopicSwitches(linearLayout: LinearLayout, topicArrayList: List<TopicModel>){
        for(topic in topicArrayList) {
            val inflater = LayoutInflater.from(context)
            val topicCard = inflater.inflate(R.layout.topic_card, null)

            val switch = topicCard.findViewById<Switch>(R.id.topicCardSwitch)
            switch.isChecked = topic.subscribed
            switch.setOnCheckedChangeListener{buttonView, isChecked ->
                activity?.getPreferences(Context.MODE_PRIVATE)?.edit()?.apply {
                    putBoolean("topic/${topic.id}", isChecked)
                    apply()
                }
                topic.subscribed = isChecked
                addTopicSubscription(topic.binding, isChecked)
            }

            val title = topicCard.findViewById<TextView>(R.id.topicCardTitle)
            title.text = topic.title

            val description = topicCard.findViewById<TextView>(R.id.topicCardDescription)
            description.text = topic.description

            linearLayout.addView(topicCard)
        }
    }

    /**
     * Invoking service to change topic subscriptions
     *
     * @param topicName Name of the topic of which the subscription status should be changed
     * @param topicStatus If the subscription should be enabled or disabled
     */
    private fun addTopicSubscription(topicName: String, topicStatus: Boolean){
        mService?.send(Message.obtain(null, RabbitMQService.MSG_CHANGE_TOPICS, 0, 0, TopicChange(topicName, topicStatus)))
        if(topicStatus){
            Log.d("Topic", "Subscribing to topic" + topicName)
        } else {
            Log.d("Topic", "Unsubscribing from topic" + topicName)
        }
    }
}
