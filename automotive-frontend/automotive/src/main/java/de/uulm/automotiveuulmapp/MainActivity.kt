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
import androidx.appcompat.app.AppCompatActivity
import de.uulm.automotiveuulmapp.topic.TopicChange


class MainActivity : AppCompatActivity() {

    private var topics = arrayOf("Natur", "Sport")

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

        var linearLayout = findViewById<LinearLayout>(R.id.scroll_linear_layout)

        for (topicString in topics) {
            var switch = Switch(this)
            switch.text = topicString
            switch.textSize = 30F
            switch.setOnCheckedChangeListener{buttonView, isChecked ->
                addTopic(buttonView.text.toString(), isChecked)
            }
            linearLayout.addView(switch)
        }
    }

    fun addTopic(topicName: String, topicStatus: Boolean){
        mService?.send(Message.obtain(null, MSG_CHANGE_TOPICS, 0, 0, TopicChange(topicName, topicStatus)))
        if(topicStatus){
            Log.d("Topic", "Subscribing to topic" + topicName)
        } else {
            Log.d("Topic", "Unsubscribing from topic" + topicName)
        }
    }
}
