package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*


class MainActivity : AppCompatActivity() {

    val serverURI = "tcp://68fd7a21-267a-45d4-bbd7-d8331d9e2d3f.ul.bw-cloud-instance.org:1883"
    val topic = "test"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // initializes mqtt connection to the server specified in the serverURI String above
        // if the connection is established, the client subscribes to the topic specified in the topic String above
        val mqttClient = mqttInit()

        // setting up callback
        // if message is being received, it is getting displayed as a toast message
        mqttMessageCallback(mqttClient)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun mqttInit(): MqttAndroidClient{
        val clientId = MqttClient.generateClientId()
        val client = MqttAndroidClient(
            this.applicationContext, serverURI,
            clientId
        )

        try {
            val token = client.connect()
            token.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    // We are connected
                    Log.d("First Fragment", "onSuccess")
                    mqttSub(topic,1, client)
                }

                override fun onFailure(
                    asyncActionToken: IMqttToken,
                    exception: Throwable
                ) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d("First Fragment", "onFailure")
                }
            }
        } catch (e: MqttException) {
            e.printStackTrace()
        }

        return client
    }

    fun mqttSub(topic: String, qos: Int, client: MqttAndroidClient){
        try {
            val subToken: IMqttToken = client.subscribe(topic, qos)
            subToken.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Log.d("MqttSub", "Successfully subscribed to topic $topic")
                }

                override fun onFailure(
                    asyncActionToken: IMqttToken,
                    exception: Throwable
                ) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards
                }
            }
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun mqttMessageCallback(mqttClient: MqttAndroidClient){
        mqttClient.setCallback(object : MqttCallback{
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Toast.makeText(applicationContext, "$topic: $message", Toast.LENGTH_SHORT).show()
            }

            override fun connectionLost(cause: Throwable?) {
                TODO("Not yet implemented")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                TODO("Not yet implemented")
            }

        })
    }
}
