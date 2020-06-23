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
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import de.uulm.automotiveuulmapp.httpHandling.CustomJsonRequest
import de.uulm.automotiveuulmapp.rabbitmq.RabbitMQService
import de.uulm.automotiveuulmapp.topic.TopicChange
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

        Intent(this, RabbitMQService::class.java).also { intent ->
            Log.d("Service", "Start Service...")
            startService(intent)
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }
    }

    /**
     * Invoking service to change topic subscriptions
     *
     * @param topicName Name of the topic of which the subscription status should be changed
     * @param topicStatus If the subscription should be enabled or disabled
     */
    fun addTopicSubscription(topicName: String, topicStatus: Boolean){
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
     * @param callback Function that should be executed with the return value as parameter
     * @param body The Object in Json-Format to be sent within the http-body
     */
    fun callRestEndpoint(url: String, httpMethod: Int, callback: (response: JSONObject) -> Unit, body: JSONObject? = null){
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)

        val customJsonRequest =
            CustomJsonRequest(httpMethod,
                url,
                body,
                Response.Listener<JSONObject> { response ->
                    callback(response)
                },
                Response.ErrorListener { error ->
                    Log.d("Error", error.toString())
                })
        // Add the request to the RequestQueue
        queue.add(customJsonRequest)
    }
}
