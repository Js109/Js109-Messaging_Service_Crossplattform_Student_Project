package de.uulm.automotiveuulmapp.messages

import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationManagerCompat
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import de.uulm.automotive.cds.entities.MessageSerializable
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.MainActivity
import de.uulm.automotiveuulmapp.messages.messagedb.MessageDatabase
import java.net.URL

class MessageContentActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_MESSAGE = "de.uulm.automotiveuulmapp.messages.extra.MESSAGE"
        const val EXTRA_PERSISTED_MESSAGE_ID = "de.uulm.automotiveuulmapp.messages.extra.MESSAGE_ID"
        const val EXTRA_NOTIFICATION_ID = "de.uulm.automotiveuulmapp.messages.extra.NOTIFICATION_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_content)

        // close notification if Activity was started from one
        if (intent.hasExtra(EXTRA_NOTIFICATION_ID)) {
            NotificationManagerCompat.from(this).cancel(
                intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0)
            )
        }

        when {
            intent.hasExtra(EXTRA_MESSAGE) -> {
                val message = intent.getSerializableExtra(EXTRA_MESSAGE) as MessageSerializable
                setupView(message)
            }
            intent.hasExtra(EXTRA_PERSISTED_MESSAGE_ID) -> {
                AsyncTask.execute {
                    val dao = MessageDatabase.getDaoInstance(this)
                    val me = dao.get(
                        intent.getIntExtra(
                            EXTRA_PERSISTED_MESSAGE_ID, 0
                        )
                    )
                    if (!me.read) {
                        dao.update(me.apply { read = true })
                    }
                    runOnUiThread {
                        setupView(
                            MessageSerializable(
                                me.sender,
                                me.title,
                                me.messageText,
                                me.attachment,
                                me.links,
                                null
                            )
                        )
                    }
                }
            }
        }
    }

    private fun setupView(message: MessageSerializable) {
        val titleView = findViewById<TextView>(R.id.messageContentTitleText)
        val messageContentView = findViewById<TextView>(R.id.messageContentText)

        titleView.text = message.title
        messageContentView.text = message.messageText
        createImageView(message.attachment)

        initializeCloseButton()

        // enable persistence button only if message does not come from database
        initializePersistenceButton(intent.hasExtra(EXTRA_MESSAGE), message)

        createMapView(message.links, message.title)
    }

    /**
     * If image data is not empty decode base64-encoded byte array and set as content of the ImageView.
     */
    private fun createImageView(image: ByteArray?) {
        if (image != null && image.isNotEmpty()) {
            val bmp =
                BitmapFactory.decodeByteArray(image, 0, image!!.size)

            val imageView = findViewById<ImageView>(R.id.messageContentImageView)
            imageView.setImageBitmap(bmp)
        } else {
            findViewById<ImageView>(R.id.messageContentImageView).visibility = View.GONE
        }
    }

    /**
     * Creates a google maps view from the first maps url in the links array.
     * If no maps url is found no map will be shown.
     */
    private fun createMapView(links: Array<URL>?, markerText: String) {
        val map =
            supportFragmentManager.findFragmentById(R.id.message_map) as SupportMapFragment
        val mapsUrl = links?.firstOrNull { isGoogleMapsUrl(it) }
        mapsUrl?.let {
            getCoordinatesFromMapsUrl(it)
        }?.let { coords ->
            map.getMapAsync { map ->
                val location = LatLng(coords.first, coords.second)
                val marker = map.addMarker(MarkerOptions().position(location).title(markerText))
                marker.showInfoWindow()
                map.moveCamera(CameraUpdateFactory.newLatLng(location))
                map.moveCamera(CameraUpdateFactory.zoomTo(17.0f))
            }

        } ?: run {
            findViewById<ConstraintLayout>(R.id.message_map_container).visibility = View.GONE
        }
    }

    private fun isGoogleMapsUrl(url: URL): Boolean {
        if (!(url.host == "google.com" || url.host == "www.google.com"))
            return false
        val pathSplit = url.path.split("/").filter { it.isNotEmpty() }
        if (pathSplit.isEmpty())
            return false
        return pathSplit[0] == "maps"
    }

    private fun getCoordinatesFromMapsUrl(url: URL): Pair<Double, Double>? {
        val pathSplit = url.path.split("/")
        val coords = pathSplit.first { it.startsWith("@") }
        val coordsSplit = coords.removeRange(0..0).split(",")
        return if (coordsSplit.size < 2) {
            null
        } else {
            Pair(coordsSplit[0].toDouble(), coordsSplit[1].toDouble())
        }
    }

    /**
     * Sets up handler for the close button of the activity
     *
     */
    private fun initializeCloseButton() {
        // button to return to topic selection activity (SubscribeActivity)
        val closeButton = findViewById<Button>(R.id.cose_message_button)
        closeButton.setOnClickListener {
            finish()
        }
    }

    /**
     * Sets up handler for the save button of the message
     *
     */
    private fun initializePersistenceButton(
        shouldBeEnabled: Boolean,
        message: MessageSerializable
    ) {
        val persistenceButton = findViewById<Button>(R.id.persist_message_button)
        persistenceButton.isEnabled = shouldBeEnabled
        persistenceButton.setOnClickListener { v: View ->
            run {
                if (v.isEnabled) {
                    MessagePersistenceService.startActionPersist(this, message)
                    v.isEnabled = false
                    Toast.makeText(applicationContext, "Message saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Message has already been saved",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
