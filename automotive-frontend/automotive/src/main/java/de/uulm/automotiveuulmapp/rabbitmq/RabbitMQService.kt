package de.uulm.automotiveuulmapp.rabbitmq

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.*
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rabbitmq.client.*
import de.uulm.automotive.cds.entities.MessageSerializable
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.geofencing.CurrentLocationFetcher
import de.uulm.automotiveuulmapp.geofencing.LocationDataFencer
import de.uulm.automotiveuulmapp.locationFavourites.locationFavData.LocationDatabase
import de.uulm.automotiveuulmapp.messages.MessageContentActivity
import de.uulm.automotiveuulmapp.messages.MessagePersistenceService
import de.uulm.automotiveuulmapp.messages.messagedb.MessageDatabase
import de.uulm.automotiveuulmapp.messages.messagedb.MessageEntity
import de.uulm.automotiveuulmapp.notifications.DismissNotificationService
import de.uulm.automotiveuulmapp.topic.TopicChange
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.time.ZoneId
import kotlin.random.Random.Default.nextInt

class RabbitMQService : Service() {

    // used to store constants
    companion object {
        const val CHANNEL_ID = "123"

        const val AMQ_HOST = "134.60.157.15"
        const val AMQ_USER = "android_cl"
        const val AMQ_PASSWORD = "supersecure"
        const val EXCHANGE_NAME = "amq.topic"

        const val MSG_INIT_AMQP = 0
        const val MSG_CHANGE_TOPICS = 1
    }

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private lateinit var channel: Channel
    private lateinit var queueName: String

    private var locationFencer: LocationDataFencer? = null

    /**
     * Handler that receives messages from the thread
     *
     */
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_INIT_AMQP -> {
                    channel = amqSetup()
                    amqSub()
                }
                MSG_CHANGE_TOPICS -> {
                    changeSubscription(msg.obj as TopicChange)
                }
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1)
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return Messenger(serviceHandler).binder
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

        locationFencer = LocationDataFencer(
            CurrentLocationFetcher(applicationContext),
            LocationDatabase.getDatabaseInstance(applicationContext)
        )

        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, getString(R.string.service_start_toast), Toast.LENGTH_SHORT).show()
        queueName = "id/" + intent!!.extras!!["queueId"].toString()

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.what = MSG_INIT_AMQP
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    /**
     * Creating the connection channel with the within the companion object defined constants.
     * This connection channel is required to interact with the queue (subscribing, changing bindings)
     */
    fun amqSetup(): Channel {
        // setup connection params
        val factory = ConnectionFactory()
        factory.host = AMQ_HOST
        factory.username = AMQ_USER
        factory.password = AMQ_PASSWORD

        // open new connection to broker
        val connection = factory.newConnection()
        val channel = connection.createChannel()

        channel.queueDeclare(queueName, true, false, false, null)
        return channel
    }

    /**
     * Defines the callback which should be executed when message is received from queue and the subscription on the queue is enabled
     *
     */
    fun amqSub() {
        // define callback which should be executed when message is received from queue
        val deliverCallback =
            DeliverCallback { _: String?, delivery: Delivery ->
                val message = convertByteArrayToMessage(delivery.body)
                // return if endtime in past
                message.endtime?.let {
                    it.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        .minus(System.currentTimeMillis())
                        .takeIf { timeDiff -> timeDiff < 0 }
                        ?.let { return@DeliverCallback }
                }
                // if no location data or if current location is in location data show as heads up notification else store message if a stored location is in range of the location data
                if (message.locationData == null || locationFencer?.currentPositionFencing(message.locationData) == true) {
                    notify(message)
                } else if (locationFencer?.storedLocationsFencing(message.locationData) == true) {
                    MessageDatabase.getDaoInstance(this).insert(
                        MessageEntity(
                            null,
                            message.sender,
                            message.title,
                            message.messageText,
                            message.attachment,
                            message.logoAttachment,
                            message.links,
                            false,
                            false,
                            message.messageDisplayProperties?.fontColor,
                            message.messageDisplayProperties?.backgroundColor,
                            message.messageDisplayProperties?.fontFamily,
                            message.messageDisplayProperties?.alignment
                        )
                    )
                }
            }

        Log.d("AMQP", " [*] Waiting for messages. To exit press CTRL+C")
        // start subscription on queue
        channel.basicConsume(queueName, true, deliverCallback, CancelCallback { })
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
    private fun notify(message: MessageSerializable) {
        // notificationId is a unique int for each notification that you must define
        val notificationId = nextInt()

        val showMessageContentIntent = Intent(this, MessageContentActivity::class.java)
        showMessageContentIntent.putExtra(MessageContentActivity.EXTRA_MESSAGE, message)
        showMessageContentIntent.putExtra(MessageContentActivity.EXTRA_NOTIFICATION_ID, notificationId)


        val pendingShowMessageContentIntent: PendingIntent =
            PendingIntent.getActivity(
                this,
                1,
                showMessageContentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        val storeMessageIntent = PendingIntent.getService(
            this,
            0,
            MessagePersistenceService.generateNotificationPersistIntent(this, message, notificationId),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val storeAction = NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_save,
            "Save",
            storeMessageIntent
        ).build()

        val dismissMessageIntent = PendingIntent.getService(
            this,
            0,
            DismissNotificationService.generateDismissNotificationIntent(this, notificationId),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val dismissAction = NotificationCompat.Action.Builder(
            android.R.drawable.ic_delete,
            "Dismiss",
            dismissMessageIntent
        ).build()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeByteArray(message.logoAttachment, 0, message.logoAttachment?.size ?: 0))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingShowMessageContentIntent)
            .setCategory(Notification.CATEGORY_CALL)
            .setContentTitle(message.title)
            .setContentText(message.messageText)
            .addAction(storeAction)
            .addAction(dismissAction)


        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
        Log.d("Notification", "Notification should be shown")
    }

    /**
     * This method adds or removes a binding for a queue to a topic
     *
     * @param topicChange Whether the subscription should be changed to active or inactive
     */
    fun changeSubscription(topicChange: TopicChange) {
        val c = channel
        when {
            topicChange.active -> {
                c.queueBind(queueName, EXCHANGE_NAME, topicChange.name)
            }
            !topicChange.active -> {
                c.queueUnbind(queueName, EXCHANGE_NAME, topicChange.name)
            }
        }
    }

    private fun convertByteArrayToMessage(byteArray: ByteArray): MessageSerializable {
        val bis = ByteArrayInputStream(byteArray)
        try {
            val input = ObjectInputStream(bis)
            val obj = input.readObject()
            return obj as MessageSerializable
        } finally {
            try {
                bis.close()
            } catch (ex: IOException) {
                // ignore close exception
            }
        }
    }
}
