package de.uulm.automotiveuulmapp.messages

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import de.uulm.automotive.cds.entities.MessageSerializable
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.SubscribeActivity
import java.net.URL

class MessageContentActivity : AppCompatActivity() {
    lateinit var message: MessageSerializable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_content)

        message = intent.getSerializableExtra("message") as MessageSerializable
        val titleView = findViewById<TextView>(R.id.messageContentTitleText)
        val messageContentView = findViewById<TextView>(R.id.messageContentText)

        titleView.text = message.title
        messageContentView.text = message.messageText
        // if attachment exists, decode base64-encoded byte array
        val image = message.attachment
        if(image != null && image.isNotEmpty()) {
            val bmp =
                BitmapFactory.decodeByteArray(message.attachment, 0, message.attachment!!.size)

            val imageView = findViewById<ImageView>(R.id.messageContentImageView)
            imageView.setImageBitmap(bmp)
        } else {
            findViewById<ImageView>(R.id.messageContentImageView).visibility = View.GONE
        }

        initializeActionViews()

        val map =
            supportFragmentManager.findFragmentById(R.id.message_map) as SupportMapFragment

        val mapsUrl = message.links?.firstOrNull { isGoogleMapsUrl(it) }
        mapsUrl?.let {
            getCoordinatesFromMapsUrl(it)
        }?.let { coords ->
            map.getMapAsync { map ->
                val location = LatLng(coords.first, coords.second)
                val marker = map.addMarker(MarkerOptions().position(location).title(message.title))
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
     * Sets up handler for close- and save-button of message
     *
     */
    private fun initializeActionViews(){
        // button to return to topic selection activity (SubscribeActivity)
        val closeButton: Button = findViewById<Button>(R.id.cose_message_button)
        closeButton.setOnClickListener {
            val intent = Intent(this, SubscribeActivity::class.java )
            startActivity(intent)
        }

        val persistenceButton = findViewById<Button>(R.id.persist_message_button)
        persistenceButton.setOnClickListener { v: View -> run {
            if(v.isEnabled){
                MessagePersistenceService.startActionPersist(this, message)
                v.isEnabled = false
                Toast.makeText(applicationContext,"Message saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext,"Message has already been saved", Toast.LENGTH_SHORT).show()
            }
        }}
    }
}
