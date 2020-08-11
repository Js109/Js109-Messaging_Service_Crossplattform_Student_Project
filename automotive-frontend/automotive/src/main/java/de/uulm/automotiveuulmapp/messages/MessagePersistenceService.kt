package de.uulm.automotiveuulmapp.messages

import android.app.IntentService
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.room.Room
import de.uulm.automotive.cds.entities.MessageSerializable
import de.uulm.automotiveuulmapp.messages.messagedb.MessageDatabase
import de.uulm.automotiveuulmapp.messages.messagedb.MessageEntity


/**
 * This service is responsible for persisting and loading messages from the database.
 *
 */
class MessagePersistenceService : IntentService("MessagePersistenceService") {

    lateinit var db: MessageDatabase

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(applicationContext, MessageDatabase::class.java, "message-db").build()
    }

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_PERSIST -> {
                val msg = intent.getSerializableExtra(EXTRA_MESSAGE) as MessageSerializable
                handleActionPersist(msg)
            }
            ACTION_READ -> {
                val messages= handleActionRead()
                sendBroadcast(ParcelableMessage.convertToParcelableArray(messages))
            }
            ACTION_DELETE -> {
                if(intent.hasExtra(EXTRA_MESSAGE_ID)){
                    val msgId = intent.extras?.getInt(EXTRA_MESSAGE_ID)
                    handleActionDelete(msgId!!)
                }
            }
        }
    }

    /**
     * Used to notify activity via a local broadcast intent
     * when the persisted messages have been loaded.
     *
     * @param messages Message parcelables read from the db
     */
    private fun sendBroadcast(messages: ArrayList<ParcelableMessage>) {
        val intent = Intent("load_messages") //put the same message as in the filter you used in the activity when registering the receiver
        intent.putParcelableArrayListExtra("messages", messages)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    /**
     * Persisting message into the room database
     */
    private fun handleActionPersist(msg: MessageSerializable) {
        val msgEntity = MessageEntity(null, msg.sender!!, msg.title!!, msg.messageText,msg.attachment, msg.links )
        db.messageDao().insert(msgEntity)
    }

    /**
     * Reading and returning all existing messages from the database
     */
    private fun handleActionRead(): List<MessageEntity> {
        return db.messageDao().getAll()
    }

    private fun handleActionDelete(messageId: Int) {
        db.messageDao().delete(messageId)
    }


    companion object {
        // Action names that describe tasks that this IntentService can perform
        private const val ACTION_PERSIST = "de.uulm.automotiveuulmapp.action.PERSIST"
        private const val ACTION_READ = "de.uulm.automotiveuulmapp.action.READ"
        private const val ACTION_DELETE = "de.uulm.automotiveuulmapp.action.DELETE"

        // Parameters this IntentService can accept
        private const val EXTRA_MESSAGE = "de.uulm.automotiveuulmapp.extra.MESSAGE"
        private const val EXTRA_MESSAGE_ID = "de.uulm.automotiveuulmapp.extra.MESSAGE_ID"
        /**
         * Starts this service to perform action Persist with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        @JvmStatic
        fun startActionPersist(context: Context, msg: MessageSerializable) {
            val intent = Intent(context, MessagePersistenceService::class.java).apply {
                action = ACTION_PERSIST
                putExtra(EXTRA_MESSAGE, msg)
            }
            context.startService(intent)
        }

        /**
         * Starts this service to perform action Read. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        @JvmStatic
        fun startActionRead(context: Context) {
            val intent = Intent(context, MessagePersistenceService::class.java).apply {
                action = ACTION_READ
            }
            context.startService(intent)
        }

        /**
         * Starts this service to perform action Delete. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        @JvmStatic
        fun startActionDelete(context: Context, messageId: Int) {
            val intent = Intent(context, MessagePersistenceService::class.java).apply {
                action = ACTION_READ
                putExtra(EXTRA_MESSAGE_ID, messageId)
            }
            context.startService(intent)
        }
    }
}
