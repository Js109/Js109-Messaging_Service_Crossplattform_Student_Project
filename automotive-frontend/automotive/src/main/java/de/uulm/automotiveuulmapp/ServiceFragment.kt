package de.uulm.automotiveuulmapp

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Messenger
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.VolleyError
import de.uulm.automotiveuulmapp.data.RegistrationData
import de.uulm.automotiveuulmapp.data.RegistrationDatabase
import de.uulm.automotiveuulmapp.httpHandling.RestCallHelper
import de.uulm.automotiveuulmapp.rabbitmq.RabbitMQService
import de.uulm.automotiveuulmapp.topic.Callback
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

class ServiceFragment : BaseFragment() {
    private lateinit var mContext: Context
    var mService: Messenger? = null
    var bound: Boolean = false

    private lateinit var db: RegistrationDatabase
    private lateinit var registrationId: UUID

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
        mContext = context
        // Database instance is created
        db = Room.databaseBuilder(context, RegistrationDatabase::class.java, ApplicationConstants.REGISTRATION_DB_NAME).build()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_service, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadRegistrationId()
    }

    /**
     * Tries to load the registration id from the database.
     * If no registration is found, a new registration request is sent to the REST API
     *
     */
    private fun loadRegistrationId() {
        // Callback function which passes the queueId to the service and starts it
        val callback = { queueId: UUID ->
            Intent(mContext, RabbitMQService::class.java).also { intent ->
                Log.d("Service", "Starting Service...")
                intent.putExtra("queueId", queueId)
                (activity as Activity).startService(intent)
                (activity as Activity).bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
            }
        }

        launch {
            val registrations = db.getRegistrationDAO().getAll()
            if(registrations.isNotEmpty()){
                registrationId = registrations[0].queueId
                callback(registrationId)
                Log.d("Registration", "Id loaded from DB")
            } else {
                Log.d("Registration", "Registering at the backend...")
                register(callback)
            }
        }
    }

    /**
     * Sends a request to register the client
     * Should be done only once, the id returned by the api should be stored
     *
     * @param callback This callback is executed when the response arrives
     */
    private fun register(callback: (UUID)->Intent){
        val url = ApplicationConstants.ENDPOINT_SIGNUP

        val json = JSONObject()
        json.put("signUpToken", UUID.randomUUID())
        json.put("deviceType", ApplicationConstants.DEVICE_TYPE)
        val restCallHelper = RestCallHelper()
        restCallHelper.callRestEndpoint(url, Request.Method.POST, object: Callback{
            override fun onSuccess(response: JSONObject) {
                val signUpToken = UUID.fromString(response["signUpToken"] as String)
                val queueId = UUID.fromString(response["queueID"] as String)
                launch {
                    db.getRegistrationDAO().insert(RegistrationData(signUpToken, queueId))
                }
                callback(queueId)
            }
            override fun onFailure(volleyError: VolleyError) {
                // TODO retry registration later
            }
        }, body = json, context = mContext)
    }
}
