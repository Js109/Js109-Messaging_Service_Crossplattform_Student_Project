package de.uulm.automotiveuulmapp.topic

import com.android.volley.VolleyError
import org.json.JSONObject

interface Callback {
    fun onSuccess(jsonObject: JSONObject)
    fun onFailure(volleyError: VolleyError)
}