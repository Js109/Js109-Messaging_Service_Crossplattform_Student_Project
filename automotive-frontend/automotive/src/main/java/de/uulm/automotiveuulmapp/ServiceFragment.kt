package de.uulm.automotiveuulmapp

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
import de.uulm.automotiveuulmapp.data.RegistrationData
import de.uulm.automotiveuulmapp.data.RegistrationDatabase
import de.uulm.automotiveuulmapp.rabbitmq.RabbitMQService
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
        db = Room.databaseBuilder(context, RegistrationDatabase::class.java, "registrationData").build()
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
        val callback = { registrationId: UUID ->
            Intent(mContext, RabbitMQService::class.java).also { intent ->
                Log.d("Service", "Start Service...")
                intent.putExtra("RegistrationId", registrationId)
                (activity as MainActivity).startService(intent)
                (activity as MainActivity).bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
            }
        }

        launch {
            val registrations = db.getRegistrationDAO().getAll()
            if(registrations.isNotEmpty()){
                registrationId = registrations.get(0).id
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
    fun register(callback: (UUID)->Intent){
        val url = getString(R.string.server_url) + "/signup"

        val json = JSONObject()
        json.put("id", 12345)
        json.put("deviceType", "Android Emulator")
        (activity as MainActivity).callRestEndpoint(url, Request.Method.POST, { response ->
            // TODO add real uuid instead of mock
            val mockId = UUID.randomUUID()
            launch {
                db.getRegistrationDAO().insert(RegistrationData(mockId))
            }
            callback(mockId)
        }, body = json)
    }
}
