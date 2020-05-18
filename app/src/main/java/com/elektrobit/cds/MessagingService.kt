package com.elektrobit.cds

import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("Firebase Messaging", message.toString())
        message.notification?.let {
            Log.d("Firebase Messaging", it.body)
            val notificationBuilder = NotificationCompat.Builder(applicationContext, "Firebase POC")
                .setSmallIcon(R.drawable.googleg_standard_color_18)
                .setContentTitle(it.title)
                .setContentText(it.body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            NotificationManagerCompat.from(applicationContext).notify(1, notificationBuilder.build())
        }
    }

}