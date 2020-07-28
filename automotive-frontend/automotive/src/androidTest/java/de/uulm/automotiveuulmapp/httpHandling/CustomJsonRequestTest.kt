package de.uulm.automotiveuulmapp.httpHandling

import android.content.Context
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import de.uulm.automotiveuulmapp.topicFragment.TopicAdapter
import de.uulm.automotiveuulmapp.topicFragment.TopicFragment
import de.uulm.automotiveuulmapp.topic.Callback
import de.uulm.automotiveuulmapp.topic.TopicModel
import org.json.JSONObject
import org.junit.BeforeClass
import org.junit.Test

class CustomJsonRequestTest {
    companion object{
        lateinit var context: Context
        lateinit var topicFragment: TopicFragment
        @JvmStatic
        @BeforeClass
        fun init(){
            context = InstrumentationRegistry.getInstrumentation().targetContext
            topicFragment =
                TopicFragment()
            topicFragment.onAttach(context)
        }
    }



    @Test
    fun oneTopicAvailable() {
        val referenceTopicList = listOf(TopicModel(1, "Test", "test/test", "description", arrayOf("test"), false))
        val jsonObject = JSONObject("{\"array\":[{\"id\": 1,\"binding\": \"test/test\",\"title\": \"test\",\"tags\": [\"test\"],\"description\": \"description\"}]}")

        val mockRestCallHelper = mock<RestCallHelper>()
        whenever(mockRestCallHelper.callRestEndpoint(any(), any(), any(), anyOrNull())).then {
            (it.arguments[2] as Callback).onSuccess(jsonObject)
        }

        val topicAdapter =
            TopicAdapter(
                topicFragment,
                SearchView(context),
                mockRestCallHelper
            )
        val recyclerView = RecyclerView(context)
        recyclerView.adapter = topicAdapter

        val topicListField = TopicAdapter::class.java.getDeclaredField("currentList")
        topicListField.isAccessible = true
        val topicListFieldValue: List<TopicModel> = topicListField.get(topicAdapter) as List<TopicModel>
        assert(referenceTopicList == topicListFieldValue)
    }

}