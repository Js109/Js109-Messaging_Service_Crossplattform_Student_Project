package de.uulm.automotiveuulmapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Switch
import com.android.volley.Request
import de.uulm.automotiveuulmapp.topic.TopicModel
import org.json.JSONArray
import org.json.JSONObject

class TopicFragment : BaseFragment() {
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_topic, container, false)
        loadAvailableTopics(v)
        return v
    }

    /**
     * Adding switches to the view to change subscriptions of the device.
     *
     * @param view View to which the
     * @param topicArrayList List of topics available to subscribe to
     */
    fun addTopicSwitches(view: View, topicArrayList: ArrayList<TopicModel>){
        val linearLayout = view.findViewById<LinearLayout>(R.id.scroll_linear_layout)

        for (topic in topicArrayList) {
            val switch = Switch(mContext)
            switch.text = topic.binding
            switch.textSize = 30F
            switch.setOnCheckedChangeListener{buttonView, isChecked ->
                (activity as MainActivity).addTopicSubscription(buttonView.text.toString(), isChecked)
            }
            linearLayout.addView(switch)
        }
    }

    /**
     * Requests all available topics from the backend api
     *
     * @param view The view where the topic listing should be added to
     */
    fun loadAvailableTopics(view: View){
        val url = getString(R.string.server_url) + "/topic"

        (activity as MainActivity).callRestEndpoint(url, Request.Method.GET, { response: JSONObject ->
            val jsonArray = JSONArray(response.get("array").toString())
            val topicArrayList = ArrayList<TopicModel>()
            for (i in 0 until jsonArray.length()){
                val element: JSONObject = jsonArray.optJSONObject(i)
                val tags:ArrayList<String> = ArrayList()
                for(tag in 0 until element.getJSONArray("tags").length()){
                    tags.add(element.getJSONArray("tags").get(i) as String)
                }
                val topic = TopicModel(
                    element.getLong("id"),
                    element.getString("binding"),
                    element.getString("description"),
                    tags.toTypedArray())
                topicArrayList.add(topic)
            }
            addTopicSwitches(view, topicArrayList)
        })
    }
}
