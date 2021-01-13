package de.uulm.automotiveuulmapp.notifications

import android.app.IntentService
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import de.uulm.automotiveuulmapp.messages.MessageContentActivity

class DismissNotificationService: IntentService("DismissNotificationService") {
    override fun onHandleIntent(intent: Intent?) {
        if(intent?.hasExtra(EXTRA_NOTIFICATION_ID) == true) {
            NotificationManagerCompat.from(this).cancel(
                intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0)
            )
        }
    }

    companion object{
        const val EXTRA_NOTIFICATION_ID = "de.uulm.automotiveuulmapp.notification.extra.NOTIFICATION_ID"

        fun generateDismissNotificationIntent(context: Context, notificationId: Int): Intent {
            return Intent(context, DismissNotificationService::class.java).apply {
                putExtra(EXTRA_NOTIFICATION_ID, notificationId)
            }
        }
    }
}