package de.uulm.automotiveuulmapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MessageContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_content)
        /*val view = View.inflate(this, R.layout.activity_message_content,null) as ViewGroup
        val imageByteArray = intent.getByteArrayExtra("message")
        val  imageView = ImageView(this);
        val bmp = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)

        imageView.visibility = View.VISIBLE
        imageView.setImageBitmap(bmp)

        view.addView(imageView)
        */
        val imageByteArray = intent.getByteArrayExtra("message")
        val bmp = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)

        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageBitmap(bmp)

    }
}
