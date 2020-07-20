package de.uulm.automotiveuulmapp.httpHandling

import android.content.Context
import com.android.volley.Network
import com.android.volley.Response
import com.android.volley.toolbox.BaseHttpStack
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.Volley
import de.uulm.automotiveuulmapp.topic.Callback
import org.json.JSONObject

class RestCallHelper (private val httpStack: BaseHttpStack?) {

    /**
     * Helper function to send http-requests to the REST-Api
     *
     * @param url Url of the rest-endpoint to be called
     * @param httpMethod HTTP Method to be used for the request
     * @param successCallback Function that should be executed with the return value as parameter
     * @param failureCallback Function that should be executed (with error object as param) when the http request fail
     * @param body The Object in Json-Format to be sent within the http-body
     */
    fun callRestEndpoint(
        url: String,
        httpMethod: Int,
        callback: Callback,
        body: JSONObject? = null,
        context: Context,
        httpStack: BaseHttpStack? = null
    ) {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(context, httpStack)

        val customJsonRequest =
            CustomJsonRequest(httpMethod,
                url,
                body,
                Response.Listener<JSONObject> { response ->
                    callback.onSuccess(response)
                },
                Response.ErrorListener { error ->
                    callback.onFailure(error)
                })
        // Add the request to the RequestQueue
        queue.add(customJsonRequest)
    }
}
