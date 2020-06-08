package de.uulm.automotiveuulmapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Switch
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private var topics = arrayOf("Natur", "Sport")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Intent(this, MyService::class.java).also { intent ->
            Log.d("Service", "Starting Service...")
            startService(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        var linearLayout = findViewById<LinearLayout>(R.id.scroll_linear_layout)

        for (topicString in topics){
            var switch = Switch(this)
            switch.text = topicString
            //switch.textSize = 22 as Float
            linearLayout.addView(switch)
        }
    }

}
