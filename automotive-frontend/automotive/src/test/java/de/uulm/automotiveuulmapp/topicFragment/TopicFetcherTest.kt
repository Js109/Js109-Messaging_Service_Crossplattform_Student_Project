package de.uulm.automotiveuulmapp.topicFragment

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.*
import de.uulm.automotiveuulmapp.httpHandling.RestCallHelper
import de.uulm.automotiveuulmapp.topic.Callback
import de.uulm.automotiveuulmapp.topic.TopicModel
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.Test

class TopicFetcherTest {

    var mockRestCallHelper = mock<RestCallHelper>()

    val topicJsonResultStringTopicsWithoutSubscription =
                                """{ "array": [{
                                    |       "id": 1,
                                    |       "binding": "bind/test",
                                    |       "title": "Test title",
                                    |       "tags": ["test", "testing"],
                                    |       "description": "Description of the first topic",
                                    |       "subscribed": false
                                    |   },{
                                    |       "id": 2,
                                    |       "binding": "bind/test2",
                                    |       "title": "Second title",
                                    |       "tags": ["verify", "assert"],
                                    |       "description": "The second topic has a description too",
                                    |       "subscribed": false
                                    |}]}""".trimMargin()

    val topicJsonResultStringTopicsWithSubscription =
                                """{ "array": [{
                                    |       "id": 1,
                                    |       "binding": "bind/test",
                                    |       "title": "Test title",
                                    |       "tags": ["test", "testing"],
                                    |       "description": "Description of the first topic",
                                    |       "subscribed": false
                                    |   },{
                                    |       "id": 2,
                                    |       "binding": "bind/test2",
                                    |       "title": "Second title",
                                    |       "tags": ["verify", "assert"],
                                    |       "description": "The second topic has a description too",
                                    |       "subscribed": true
                                    |}]}""".trimMargin()

    fun setRestCallResult(jsonString: String) {
        whenever(mockRestCallHelper.callRestEndpoint(any(), any(), any(), anyOrNull())).then {
            (it.arguments[2] as Callback).onSuccess(JSONObject(jsonString))
        }
    }

    @Test
    fun testLoadsTopicsWithoutSubscription() {
        setRestCallResult(topicJsonResultStringTopicsWithoutSubscription)
        val expectedTopics = listOf(TopicModel(1, "Test title", "bind/test", "Description of the first topic", arrayOf("test", "testing"), false),
            TopicModel(2, "Second title", "bind/test2", "The second topic has a description too", arrayOf("verify", "assert"), false))
        val loader = TopicFetcher(mockRestCallHelper)
        val loadedTopics = loader.getTopics()
        assertThat(loadedTopics).isEqualTo(expectedTopics)
    }

    @Test
    fun testLoadsTopicsWithSubscription() {
        setRestCallResult(topicJsonResultStringTopicsWithSubscription)
        val expectedTopics = listOf(TopicModel(2, "Second title", "bind/test2", "The second topic has a description too", arrayOf("verify", "assert"), true),
            TopicModel(1, "Test title", "bind/test", "Description of the first topic", arrayOf("test", "testing"), false))
        val preferences = mock<SharedPreferences>()
        whenever(preferences.getBoolean(any(), any())).thenReturn(false)
        whenever(preferences.getBoolean(eq("topic/2"), any())).thenReturn(true)
        val loader = TopicFetcher(mockRestCallHelper, preferences)
        val loadedTopics = loader.getTopics()
        assertThat(loadedTopics).isEqualTo(expectedTopics)
    }

    @Test
    fun filteringIsApplied() {
        setRestCallResult(topicJsonResultStringTopicsWithSubscription)
        val expectedTopics = listOf(TopicModel(2, "Second title", "bind/test2", "The second topic has a description too", arrayOf("verify", "assert"), false))
        val loader = TopicFetcher(mockRestCallHelper)
        val loadedTopics = loader.getTopics("second")
        assertThat(loadedTopics).isEqualTo(expectedTopics)
    }

    @Test
    fun onTopicFetchedIsCalled() {
        setRestCallResult(topicJsonResultStringTopicsWithoutSubscription)
        var wasCalled = false
        val loader = TopicFetcher(mockRestCallHelper, onTopicFetched = {wasCalled = true})
        assertThat(wasCalled).isTrue()
    }
}