package de.uulm.automotiveuulmapp.topicFragment

import android.content.SharedPreferences
import de.uulm.automotiveuulmapp.httpHandling.RestCallHelper
import de.uulm.automotiveuulmapp.topic.Callback
import de.uulm.automotiveuulmapp.topic.TopicModel
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.Before
import org.junit.Test

class TopicFetcherTest {

    var mockRestCallHelper = mockk<RestCallHelper>()

    // json string that is returned for every call of callRestEndpoint of mockRestCallHelper
    val topicJsonResultString =
        """{ "array": [{
            |       "id": 1,
            |       "binding": "bind/test",
            |       "title": "Test title",
            |       "tags": ["test", "testing"],
            |       "description": "Description of the first topic"
            |   },{
            |       "id": 2,
            |       "binding": "bind/test2",
            |       "title": "Second title",
            |       "tags": ["verify", "assert"],
            |       "description": "The second topic has a description too"
            |}]}""".trimMargin()

    /**
     * Sets up the mocked RestCallHelper so that callRestEndpoint always calls onSuccess of the Callback with topicJsonResultString as the response
     */
    @Before
    fun setRestCallResult() {
        every { mockRestCallHelper.callRestEndpoint(any(), any(), any(), any()) } answers {
            (it.invocation.args[2] as Callback).onSuccess(JSONObject(topicJsonResultString))
        }
    }

    @Test
    fun testLoadsTopicsWithoutSubscription() {
        // Topics should be the same as in json string
        val expectedTopics = listOf(
            TopicModel(
                1,
                "Test title",
                "bind/test",
                "Description of the first topic",
                arrayOf("test", "testing"),
                false,
                false
            ),
            TopicModel(
                2,
                "Second title",
                "bind/test2",
                "The second topic has a description too",
                arrayOf("verify", "assert"),
                false,
                false
            )
        )
        // Creates a TopicFetcher without SharedPreferences so all topics returned should have subscribed set to false
        val loader = TopicFetcher(mockRestCallHelper)
        // Gets Topics without query so all topics should be returned
        val loadedTopics = loader.getTopics()
        assertThat(loadedTopics).isEqualTo(expectedTopics)
    }

    @Test
    fun testLoadsTopicsWithSubscription() {
        // topics should match those in json string, but topic with id 2 should be first, as subscribed topics are sorted to the front
        val expectedTopics = listOf(
            TopicModel(
                2,
                "Second title",
                "bind/test2",
                "The second topic has a description too",
                arrayOf("verify", "assert"),
                true,
                false
            ),
            TopicModel(
                1,
                "Test title",
                "bind/test",
                "Description of the first topic",
                arrayOf("test", "testing"),
                false,
                false
            )
        )
        // creates mock preferences to return true for the boolean of topic with id 2 and false for every other string
        val preferences = mockk<SharedPreferences>()
        every { preferences.getBoolean(any(), any()) } returns false
        every { preferences.getBoolean(eq("topic/2"), any()) } returns true
        // creates a TopicFetcher with the mocked SharedPreferences so topic 2 should have subscribed true
        val loader = TopicFetcher(mockRestCallHelper, preferences)
        val loadedTopics = loader.getTopics()
        assertThat(loadedTopics).isEqualTo(expectedTopics)
    }

    @Test
    fun filteringIsApplied() {
        // only topic 2 should be returned, as only it contains the keyword 'second'
        val expectedTopics = listOf(
            TopicModel(
                2,
                "Second title",
                "bind/test2",
                "The second topic has a description too",
                arrayOf("verify", "assert"),
                false,
                false
            )
        )
        val loader = TopicFetcher(mockRestCallHelper)
        val loadedTopics = loader.getTopics("second")
        assertThat(loadedTopics).isEqualTo(expectedTopics)
    }

    @Test
    fun onTopicFetchedIsCalled() {
        // test if the onTopicFetched is called
        var wasCalled = false
        TopicFetcher(mockRestCallHelper, onTopicFetched = { wasCalled = true })
        assertThat(wasCalled).isTrue()
    }
}