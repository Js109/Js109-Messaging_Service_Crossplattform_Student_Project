package de.uulm.automotiveuulmapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import de.uulm.automotiveuulmapp.httpHandling.CustomJsonRequest
import org.json.JSONObject


/**
 * This is the MainActivity, which is the starting point of the application.
 * The onCreate() method is called first
 */
class MainActivity : AppCompatActivity() {
    private val TIME_OUT = 4000 //Time to launch the another activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)
        setContentView(R.layout.activity_welcome)

        val view = findViewById<View>(R.id.welcome_videoView) as VideoView
        val path = "android.resource://" + packageName + "/" + R.raw.video_file
        view.setVideoURI(Uri.parse(path))
        view.start()

        // val myLayout = findViewById<View>(R.id.welcome_activity_slilder)
        Handler().postDelayed(Runnable {
            // play the Video as starting point
            val i = Intent(this@MainActivity, WelcomeAppIntro::class.java)
            startActivity(i)
            finish()
        }, TIME_OUT.toLong())
    }

    /**
     * Helper function to send http-requests to the REST-Api
     *
     * @param url Url of the rest-endpoint to be called
     * @param httpMethod HTTP Method to be used for the request
     * @param successCallback Function that should be executed with the return value as parameter
     * @param failureCallback Function that should be executed (with error object as param) when the http request fail
     * @param body The Object in Json-Format to be sent within the http-body
     */
    fun callRestEndpoint(url: String, httpMethod: Int, successCallback: (response: JSONObject) -> Unit, failureCallback: (error: VolleyError) -> Unit, body: JSONObject? = null){
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)

        val customJsonRequest =
            CustomJsonRequest(httpMethod,
                url,
                body,
                Response.Listener<JSONObject> { response ->
                    successCallback(response)
                },
                Response.ErrorListener { error ->
                    failureCallback(error)
                })
        // Add the request to the RequestQueue
        queue.add(customJsonRequest)
    }
}
