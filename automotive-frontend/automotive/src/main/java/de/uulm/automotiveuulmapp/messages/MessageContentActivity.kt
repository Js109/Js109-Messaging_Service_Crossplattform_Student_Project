package de.uulm.automotiveuulmapp.messages

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ImageSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import de.uulm.automotive.cds.entities.MessageDisplayPropertiesSerializable
import de.uulm.automotive.cds.entities.MessageSerializable
import de.uulm.automotive.cds.models.Alignment
import de.uulm.automotive.cds.models.getFont
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.messages.messagedb.MessageDatabase
import de.uulm.automotiveuulmapp.messages.specialContent.GoogleMapsLinkHelper
import de.uulm.automotiveuulmapp.messages.specialContent.LinkCategoryIdentifier
import de.uulm.automotiveuulmapp.messages.specialContent.LinkCategoryIdentifier.LinkCategory
import de.uulm.automotiveuulmapp.messages.specialContent.LinkCategoryIdentifier.LinkCategory.*
import java.net.URL

class MessageContentActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_MESSAGE = "de.uulm.automotiveuulmapp.messages.extra.MESSAGE"
        const val EXTRA_PERSISTED_MESSAGE_ID = "de.uulm.automotiveuulmapp.messages.extra.MESSAGE_ID"
        const val EXTRA_NOTIFICATION_ID = "de.uulm.automotiveuulmapp.messages.extra.NOTIFICATION_ID"

        private const val IMG_TAG = "[img]"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_content)
        init(this.intent)
    }

    /**
     * Called when activity is already open
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { init(intent) }
    }

    private fun init(intent: Intent) {
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
                                me.logoAttachment,
                                me.links,
                                null,
                                null,
                                MessageDisplayPropertiesSerializable(
                                    me.fontColor,
                                    me.backgroundColor,
                                    me.fontFamily,
                                    me.alignment
                                )
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
        messageContentView.movementMethod = LinkMovementMethod.getInstance()
        message.messageDisplayProperties?.alignment?.let {
            messageContentView.gravity = when(it) {
                Alignment.LEFT -> Gravity.START
                Alignment.CENTER -> Gravity.CENTER
                Alignment.RIGHT -> Gravity.END
            }
        }
        messageContentView.text = createMessageContent(message.messageText, message.links, message.attachment)

        titleView.text = message.title

        val fontColor: Int? =
            try {
                Color.parseColor(message.messageDisplayProperties?.fontColor)
            } catch (e: IllegalArgumentException) {
                null
            } catch (e: NullPointerException) {
                null
            }
        val backgroundColor: Int? =
            try {
                Color.parseColor(message.messageDisplayProperties?.backgroundColor)
            } catch (e: IllegalArgumentException) {
                null
            } catch (e: NullPointerException) {
                null
            }

        // Only change the font and background colors if both are set.
        // This check is necessary in case the user only sets one color, and the combination of the
        // set color of (font or background) with the default color of (font or background) result
        // in unreadable or badly readable text.
        if (backgroundColor != null && fontColor != null) {
            titleView?.let {
                it.rootView?.setBackgroundColor(backgroundColor)
                it.setTextColor(fontColor)
            }

            messageContentView?.setTextColor(fontColor)
        }

        titleView?.let {
            it.typeface =
                ResourcesCompat.getFont(this.applicationContext, getFont(message.messageDisplayProperties?.fontFamily))
        }

        messageContentView?.let {
            it.typeface =
                ResourcesCompat.getFont(this.applicationContext, getFont(message.messageDisplayProperties?.fontFamily))
        }

        initializeCloseButton()

        // enable persistence button only if message does not come from database
        initializePersistenceButton(intent.hasExtra(EXTRA_MESSAGE), message)

        // clears link list, image view and map view to add new elements
        preclear()
        message.links?.map { messageLink ->
            LinkCategoryIdentifier.identify(messageLink).also {
                when (it) {
                    MAPS -> createMapView(messageLink)
                    YOUTUBE -> addLink(it, messageLink)
                    BROWSER -> addLink(it, messageLink)
                }
            }
        }
    }

    /**
     * Creates the content for the message content view from the message text, links and attachment
     */
    private fun createMessageContent(messageText: String?, links: Array<URL>?, attachment: ByteArray?): SpannableString? {
        return messageText?.let { text ->
            links?.let { links -> createLinkTags(text, links) } ?: text
        }?.let { text ->
            val spannableString = SpannableString(Html.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY))
            var imgIndex = spannableString.indexOf(IMG_TAG, ignoreCase = true)
            while (imgIndex >= 0) {
                val imgSpan = ImageSpan(this, BitmapFactory.decodeByteArray(attachment, 0, attachment!!.size))
                spannableString.setSpan(imgSpan, imgIndex, imgIndex + IMG_TAG.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                imgIndex = spannableString.indexOf("[img]", ignoreCase = true, startIndex = imgIndex + 1)
            }
            spannableString
        }
    }

    /**
     * replaces every instance of the form "[text](link#)" with the respective html tag
     */
    private fun createLinkTags(messageText: String, links: Array<URL>): String {
        return messageText.replace("""(?i)\[(?<linkText>[^()\[\]]*)]\(link(?<linkId>\d+)\)""".toRegex()) {
            val linkText = it.groups[1]?.value
            val linkId = it.groups[2]?.value?.toInt()
            if (linkId != null && linkId > 0) {
                "<a href=${links[linkId - 1]}>$linkText</a>"
            } else {
                linkText ?: ""
            }
        }.replace("\n", "<br/>")
    }

    /**
     * Hides content of Map container and removes link list elements
     */
    private fun preclear() {
        findViewById<LinearLayout>(R.id.linkContainer).removeAllViews()
        findViewById<ConstraintLayout>(R.id.message_map_container).visibility = View.GONE
    }


    /**
     * Creates a google maps view from the passed link
     */
    private fun createMapView(link: URL) {
        val map =
            supportFragmentManager.findFragmentById(R.id.message_map) as SupportMapFragment

        findViewById<ConstraintLayout>(R.id.message_map_container).visibility = View.VISIBLE

        link.let {
            GoogleMapsLinkHelper.getCoordinatesFromMapsUrl(it)
        }?.let { coords ->
            map.getMapAsync { map ->
                val location = LatLng(coords.latitude, coords.longitude)
                val marker = map.addMarker(MarkerOptions().position(location))
                marker.showInfoWindow()
                map.moveCamera(CameraUpdateFactory.newLatLng(location))
                if (coords.zoomLevel != null) {
                    map.moveCamera(CameraUpdateFactory.zoomTo(coords.zoomLevel))
                } else {
                    map.moveCamera(CameraUpdateFactory.zoomTo(17.0f))
                }
            }
        }
    }

    /**
     * Adds a new link element to the layout.
     * Presentation of element depends on the Link category
     *
     * @param category Category of the Link
     * @param link Link to be added as element
     * @param linkLabel Optional string that is shown instead of the actual URL
     */
    private fun addLink(category: LinkCategory, link: URL, linkLabel: String? = null) {
        val layoutInflater = LayoutInflater.from(this)
        val linkView: View? = when (category) {
            YOUTUBE -> {
                val layout = layoutInflater.inflate(R.layout.youtube_link, null)
                layout.findViewById<TextView>(R.id.linkTextField).text = link.toString()
                layout.setOnClickListener {
                    val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse(link.toString()))
                    this.startActivity(intentApp)
                }
                layout
            }
            BROWSER -> {
                val layout = layoutInflater.inflate(R.layout.message_browser_link, null)
                layout.findViewById<TextView>(R.id.linkTextField).text =
                    HtmlCompat.fromHtml("<a href='$link'>$link</a>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                layout.findViewById<TextView>(R.id.linkTextField).movementMethod =
                    LinkMovementMethod.getInstance()
                layout
            }
            else -> null
        }
        if(linkLabel != null)
            linkView?.findViewById<TextView>(R.id.linkTextField)?.text = linkLabel
        val linkList = findViewById<LinearLayout>(R.id.linkContainer)
        linkList.addView(linkView)
    }

    /**
     * Sets up handler for the close button of the activity
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
