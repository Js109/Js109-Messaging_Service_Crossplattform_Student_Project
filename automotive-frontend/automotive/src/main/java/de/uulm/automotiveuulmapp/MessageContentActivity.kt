package de.uulm.automotiveuulmapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import de.uulm.automotive.cds.entities.MessageSerializable
import java.nio.charset.Charset

class MessageContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_content)

        val message = intent.getSerializableExtra("message") as MessageSerializable
        val titleView = findViewById<TextView>(R.id.titleText)
        val messageContentView = findViewById<TextView>(R.id.messageContentText)

        titleView.setText(message.title)
        messageContentView.setText(message.messageText)
        // if attachment exists, decode base64-encoded byte array
        message.attachment?.let{
            val bmp = BitmapFactory.decodeByteArray(message.attachment, 0, message.attachment!!.size)

            val imageView = findViewById<ImageView>(R.id.imageView)
            imageView.setImageBitmap(bmp)
        }
    }
}
