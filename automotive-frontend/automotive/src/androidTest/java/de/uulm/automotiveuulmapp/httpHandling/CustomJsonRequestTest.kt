package de.uulm.automotiveuulmapp.httpHandling

import android.content.Context
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import de.uulm.automotiveuulmapp.TopicAdapter
import de.uulm.automotiveuulmapp.TopicFragment
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
            topicFragment = TopicFragment()
            topicFragment.onAttach(context)
        }
    }



    @Test
    fun oneTopicAvailable() {
        val referenceTopicList = listOf(TopicModel(1, "Test", "test/test", "description", arrayOf("test"), false))
        val jsonObject = JSONObject("{\"array\":[{\"id\": 1,\"binding\": \"test/test\",\"title\": \"test\",\"tags\": [\"test\"],\"description\": \"description\"}]}")

        val mockRestCallHelper = mock<RestCallHelper>()
        whenever(mockRestCallHelper.callRestEndpoint(any(), any(), any(), anyOrNull(), any())).then {
            (it.arguments[2] as Callback).onSuccess(jsonObject)
        }

        val recyclerView = RecyclerView(context)
        val topicAdapter:  TopicAdapter = TopicAdapter(topicFragment, recyclerView, SearchView(context), mockRestCallHelper)
        val topicListField = TopicAdapter::class.java.getDeclaredField("topicList")
        topicListField.isAccessible = true
        val topicListFieldValue: List<TopicModel> = topicListField.get(topicAdapter) as List<TopicModel>;
        assert(referenceTopicList == topicListFieldValue)

    }

    private fun prepareByteArray(array: Array<Any>): ByteArray {
        /*val bos = ByteArrayOutputStream()
        val oos = ObjectOutputStream(bos)
        oos.writeObject(array)
        oos.flush()
        return bos.toByteArray()*/
        return array.toString().toByteArray(charset("UTF8"))
        //byteInputStream(Charset.forName("UTF8")).
    }

    /*private fun prepareJsonObject(): JSONObject {
        var js = JSONObject()
        js.put("test", "test")
        /*jsonObject.put("id", "1")
        jsonObject.put("binding", "test/test")
        jsonObject.put("title", "test")
        jsonObject.put("tags", "test")
        jsonObject.put("description", "description")*/
        return js
    }*/
}