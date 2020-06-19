package de.uulm.automotiveuulmapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import de.uulm.automotiveuulmapp.topic.TopicChange
import de.uulm.automotiveuulmapp.topic.TopicModel
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Intent(this, MyService::class.java).also { intent ->
            Log.d("Service", "Start Service...")
            startService(intent)
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStart() {
        super.onStart()
        register()
        loadAvailableTopics()

    }

    fun addTopic(topicName: String, topicStatus: Boolean){
        mService?.send(Message.obtain(null, MyService.MSG_CHANGE_TOPICS, 0, 0, TopicChange(topicName, topicStatus)))
        if(topicStatus){
            Log.d("Topic", "Subscribing to topic" + topicName)
        } else {
            Log.d("Topic", "Unsubscribing from topic" + topicName)
        }
    }

    fun addTopicSwitches(topicArrayList: ArrayList<TopicModel>){
        val linearLayout = findViewById<LinearLayout>(R.id.scroll_linear_layout)

        for (topic in topicArrayList) {
            val switch = Switch(this)
            switch.text = topic.binding
            switch.textSize = 30F
            switch.setOnCheckedChangeListener{buttonView, isChecked ->
                addTopic(buttonView.text.toString(), isChecked)
            }
            linearLayout.addView(switch)
        }
    }

    fun loadAvailableTopics(){
        val url = "http://192.168.178.25:8080/topic"

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
            addTopicSwitches(topicArrayList)
        })
    }

    fun register(){
        val url = "http://192.168.178.25:8080/signup"

        val json = JSONObject()
        json.put("id", 12345)
        json.put("deviceType", "Android Emulator")
        callRestEndpoint(url, Request.Method.POST, { response ->
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
        }, body = json)
    }

    /**
     * TODO
     *
     * @param url
     * @param httpMethod
     * @param callback
     * @param expectedReturnFormat
     * @param body
     */
    fun callRestEndpoint(url: String, httpMethod: Int, callback: (response: JSONObject) -> Unit, body: JSONObject? = null){
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)

        val customJsonRequest = CustomJsonRequest(httpMethod, url, body,
            Response.Listener<JSONObject> { response ->
                callback(response)
            },
            Response.ErrorListener{
                    error -> Log.d("Error", error.toString())
            })
        // Add the request to the RequestQueue
        queue.add(customJsonRequest)
    }
}
