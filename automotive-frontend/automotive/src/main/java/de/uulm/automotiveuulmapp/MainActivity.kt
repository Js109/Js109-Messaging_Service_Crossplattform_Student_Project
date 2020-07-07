package de.uulm.automotiveuulmapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import de.uulm.automotiveuulmapp.welcome.WelcomeAppIntro

/**
 * This is the MainActivity, which is the starting point of the application.
 * The onCreate() method is called first.
 * On the first Startup a Welcome Introduction is shown to the user controlled through
 * SharedPreferences 'isFirstRun' which will be set after the first run.
 */
class MainActivity : AppCompatActivity() {
    private val TIME_OUT = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //show introduction video
        setContentView(R.layout.activity_welcome_video)

        // read the value of SharedPreferences with the name of PREFERENCE
        val isFirstRun =
            getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .getBoolean("isFirstRun", true)

        if (isFirstRun) {
            // start the Introvideo from local storage
            val view = findViewById<View>(R.id.welcome_videoView) as VideoView
            val path = "android.resource://" + packageName + "/" + R.raw.video_file
            view.setVideoURI(Uri.parse(path))
            view.start()

            // go to the next Activity after 4 seconds, when the video is played
            Handler().postDelayed(Runnable {
                // play the Video as starting point
                val i = Intent(this@MainActivity, WelcomeAppIntro::class.java)
                startActivity(i)
                finish()
            }, TIME_OUT.toLong())
        }
        getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).edit()
            .putBoolean("isFirstRun", false).commit()

        // start the Introvideo from local storage
        val view = findViewById<View>(R.id.welcome_videoView) as VideoView
        val path = "android.resource://" + packageName + "/" + R.raw.video_file
        view.setVideoURI(Uri.parse(path))
        view.start()

        // go to the next Activity after 4 seconds, when the video is played
        Handler().postDelayed(Runnable {
            // play the Video as starting point
            val i = Intent(this@MainActivity, SubscribeActivity::class.java)
            startActivity(i)
            finish()
        }, TIME_OUT.toLong())
    }
}
