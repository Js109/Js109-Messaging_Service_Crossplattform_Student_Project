package de.uulm.automotiveuulmapp

import com.android.volley.Network
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import org.json.JSONArray
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class CustomJsonRequest(
    method: Int,
    url: String?,
    jsonRequest: JSONObject?,
    listener: Response.Listener<JSONObject>?,
    errorListener: Response.ErrorListener?
) : JsonObjectRequest(method, url, jsonRequest, listener, errorListener) {
    override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {
        var newResponse = response
        try {
            if (response.data.isEmpty()) {     // If no responseBody is received, generate an empty json object to avoid running into json parsing issues
                val responseData =
                    "{}".toByteArray(charset("UTF8"))
                newResponse = NetworkResponse(response.statusCode, responseData, response.notModified, response.networkTimeMs, response.allHeaders)
                NetworkResponse(
                    response.statusCode,
                    responseData,
                    response.notModified,
                    response.networkTimeMs,
                    response.allHeaders
                )
            } else {    // Checking if response is a json array, in which case a JsonObject is used as a wrapper and returned, containing the jsonArray as single attribute
                val jsonString = String(
                    response.data,
                    Charset.forName(HttpHeaderParser.parseCharset(response.headers, JsonRequest.PROTOCOL_CHARSET))
                )
                if(jsonString.startsWith("[")){
                    val wrapperObject = JSONObject().put("array", JSONArray(jsonString))
                    return Response.success(
                        wrapperObject, HttpHeaderParser.parseCacheHeaders(response)
                    )
                }
            }
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return super.parseNetworkResponse(newResponse)
    }
}