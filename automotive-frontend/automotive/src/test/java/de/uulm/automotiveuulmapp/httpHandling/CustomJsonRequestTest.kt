package de.uulm.automotiveuulmapp.httpHandling

import android.content.Context
import android.widget.SearchView
import com.android.volley.toolbox.HttpResponse
import de.uulm.automotiveuulmapp.TopicAdapter
import de.uulm.automotiveuulmapp.TopicFragment
import de.uulm.automotiveuulmapp.topic.TopicModel
import junit.framework.Assert.assertEquals
import org.json.JSONObject
import org.junit.BeforeClass
import org.junit.Test
import org.mockito.Mockito.mock
import java.io.ByteArrayInputStream
import java.nio.charset.Charset

class CustomJsonRequestTest {
    companion object{
        lateinit var context: Context
        lateinit var topicFragment: TopicFragment
        @JvmStatic
        @BeforeClass
        fun init(){
            context = mock(Context::class.java)
            topicFragment = TopicFragment()
            topicFragment.onAttach(context)
        }
    }

    @Test
    fun noTopicsAvailable() {
        val emptyTopicList = listOf<TopicModel>();
        val mockHttpStack = setUpMockResponse(emptyArray())

        val topicAdapter:  TopicAdapter = TopicAdapter(topicFragment, SearchView(context), mockHttpStack)
        val topicListField = TopicAdapter::class.java.getDeclaredField("topicList")
        topicListField.isAccessible = true
        val topicListFieldValue: List<TopicModel> = topicListField.get(topicAdapter) as List<TopicModel>;
        assertEquals(emptyTopicList, topicListFieldValue)
    }


    @Test
    fun oneTopicAvailable() {
        val referenceTopicList = listOf(TopicModel(1, "Test", "test/test", "description", arrayOf("test"), false))
        val jsonObject = JSONObject("{\"id\": 1,\"binding\": \"test/test\",\"title\": \"test\",\"tags\": [\"test\"],\"description\": \"description\"}")

        val array = arrayOf(jsonObject)
        val mockHttpStack = setUpMockResponse(arrayOf(jsonObject))

        val topicAdapter:  TopicAdapter = TopicAdapter(topicFragment, SearchView(context), mockHttpStack)
        val topicListField = TopicAdapter::class.java.getDeclaredField("topicList")
        topicListField.isAccessible = true
        val topicListFieldValue: List<TopicModel> = topicListField.get(topicAdapter) as List<TopicModel>;
        assertEquals(referenceTopicList, topicListFieldValue)
    }


    private fun setUpMockResponse(data: Array<Any>): MockHttpStack{
        val mockHttpStack = de.uulm.automotiveuulmapp.httpHandling.MockHttpStack()
        val byteArray = prepareByteArray(data)
        val dataIs = ByteArrayInputStream(byteArray)
        val httpResponse = HttpResponse(200, listOf(), byteArray.size, dataIs)
        mockHttpStack.setResponseToReturn(httpResponse)
        return mockHttpStack
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