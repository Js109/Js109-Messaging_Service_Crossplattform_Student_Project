package de.uulm.automotiveuulmapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.*
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import java.nio.charset.Charset
import kotlin.random.Random.Default.nextInt

class MyService : Service() {

    // used to store constants
    companion object{
        const val CHANNEL_ID = "123"
        const val AMQ_HOST = "134.60.157.15"
        const val AMQ_USER = "android_cl"
        const val AMQ_PASSWORD = "supersecure"
        const val QUEUE_NAME = "hello"
    }

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            amqSub()

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1)
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread("ServiceStartArguments", THREAD_PRIORITY_BACKGROUND).apply {
            start()

            // Get the HandlerThread's Looper and use it for our Handler
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }

        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    /**
     * Sets up the connection to the broker and sets callback when a message is received by the queue
     *
     */
    fun amqSub() {
        //probably required to be able to start a new thread to be able to wait asynchronously for the callback
        //val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        //StrictMode.setThreadPolicy(policy)

        //setup connection params
        val factory = ConnectionFactory()
        factory.host = AMQ_HOST
        factory.username = AMQ_USER
        factory.password = AMQ_PASSWORD

        //open new connection to broker
        val connection = factory.newConnection()
        val channel = connection.createChannel()

        channel.queueDeclare(QUEUE_NAME, true, false, false, null)

        //define callback which should be executed when message is received from queue
        val deliverCallback =
            DeliverCallback { _: String?, delivery: Delivery ->
                val message = String(delivery.body, Charset.forName("UTF-8"))
                notify(message)
            }

        Log.d("AMQP"," [*] Waiting for messages. To exit press CTRL+C")
        //start subscription on queue
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, CancelCallback {  })
    }

    /**
     * Creates a notification channel where notifications to the users can be published to
     * The channel later can be addressed with the CHANNELID defined in the companion object
     */
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * Takes a message and displays it as heads-up notification
     * The message is being configured to fulfill the requirements to be displayed as a heads-up notification within automotive os
     *
     * @param message Message which should be displayed in the notification
     */
    private fun notify(message: String){
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Message from Queue")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_NAVIGATION)

        val notificationId = nextInt()

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }
        Log.d("Notification","Notification should be shown")
    }


}
