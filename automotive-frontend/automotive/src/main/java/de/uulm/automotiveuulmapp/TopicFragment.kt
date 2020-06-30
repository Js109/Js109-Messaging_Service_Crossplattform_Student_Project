package de.uulm.automotiveuulmapp

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Switch
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import de.uulm.automotiveuulmapp.httpHandling.CustomJsonRequest
import de.uulm.automotiveuulmapp.rabbitmq.RabbitMQService
import de.uulm.automotiveuulmapp.topic.TopicChange
import de.uulm.automotiveuulmapp.topic.TopicModel
import org.json.JSONArray
import org.json.JSONObject

class TopicFragment : BaseFragment() {
    private lateinit var mContext: Context
    var mService: Messenger? = null
    var bound: Boolean = false

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
     * Adding switches to the view to change subscriptions of the device.
     *
     * @param view View to which the
     * @param topicArrayList List of topics available to subscribe to
     */
    private fun addTopicSwitches(view: View, topicArrayList: ArrayList<TopicModel>){
        val linearLayout = view.findViewById<LinearLayout>(R.id.scroll_linear_layout)

        for (topic in topicArrayList) {
            val switch = Switch(mContext)
            switch.text = topic.binding
            switch.textSize = 30F
            switch.setOnCheckedChangeListener{buttonView, isChecked ->
                addTopicSubscription(buttonView.text.toString(), isChecked)
            }
            linearLayout.addView(switch)
        }
    }

    /**
     * Requests all available topics from the backend api
     *
     * @param view The view where the topic listing should be added to
     */
    private fun loadAvailableTopics(view: View){
        val url = ApplicationConstants.ENDPOINT_TOPIC

        callRestEndpoint(url, Request.Method.GET, { response: JSONObject ->
            val jsonArray = JSONArray(response.get("array").toString())
            val topicArrayList = ArrayList<TopicModel>()
            for (i in 0 until jsonArray.length()){
                val element: JSONObject = jsonArray.optJSONObject(i)
                val tags:ArrayList<String> = ArrayList()
                for(tag in 0 until element.getJSONArray("tags").length()){
                    tags.add(element.getJSONArray("tags").get(i) as String)
                }
                val topic = TopicModel(
                    element.getLong("id"),
                    element.getString("binding"),
                    element.getString("description"),
                    tags.toTypedArray())
                topicArrayList.add(topic)
            }
            addTopicSwitches(view, topicArrayList)
        }, { error: VolleyError -> Log.e("Topic","Failed to load topics")})
    }

    /**
     * Invoking service to change topic subscriptions
     *
     * @param topicName Name of the topic of which the subscription status should be changed
     * @param topicStatus If the subscription should be enabled or disabled
     *
     */
    private fun addTopicSubscription(topicName: String, topicStatus: Boolean){
        mService?.send(Message.obtain(null, RabbitMQService.MSG_CHANGE_TOPICS, 0, 0, TopicChange(topicName, topicStatus)))
        if(topicStatus){
            Log.d("Topic", "Subscribing to topic" + topicName)
        } else {
            Log.d("Topic", "Unsubscribing from topic" + topicName)
        }
    }

    /**
     * Helper function to send http-requests to the REST-Api
     *
     * @param url Url of the rest-endpoint to be called
     * @param httpMethod HTTP Method to be used for the request
     * @param successCallback Function that should be executed with the return value as parameter
     * @param failureCallback Function that should be executed (with error object as param) when the http request fail
     * @param body The Object in Json-Format to be sent within the http-body
     */
    fun callRestEndpoint(url: String, httpMethod: Int, successCallback: (response: JSONObject) -> Unit, failureCallback: (error: VolleyError) -> Unit, body: JSONObject? = null){
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(mContext)

        val customJsonRequest =
            CustomJsonRequest(httpMethod,
                url,
                body,
                Response.Listener<JSONObject> { response ->
                    successCallback(response)
                },
                Response.ErrorListener { error ->
                    failureCallback(error)
                })
        // Add the request to the RequestQueue
        queue.add(customJsonRequest)
    }
}
