package de.uulm.automotiveuulmapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Intent(this, MyService::class.java).also { intent ->
            Log.d("Service","Starting Service...")
            startService(intent)
        }
    }
}
