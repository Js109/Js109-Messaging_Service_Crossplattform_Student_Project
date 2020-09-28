package de.uulm.automotiveuulmapp.topicFragment
/*
import android.content.SharedPreferences
import android.widget.SearchView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.nhaarman.mockitokotlin2.*
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.TopicAdapterTestActivity
import de.uulm.automotiveuulmapp.httpHandling.RestCallHelper
import de.uulm.automotiveuulmapp.topic.Callback
import org.hamcrest.CoreMatchers.allOf
import org.json.JSONObject
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests the behavior of the TopicAdapter by using a test Activity in which a RecyclerView has been placed.
 */
class TopicAdapterTest {
    @get:Rule
    val activityRule = ActivityTestRule(TopicAdapterTestActivity::class.java)

    private val mockRestCallHelper = mock<RestCallHelper>()

    // stores the callback passed to the mockRestCallHelper to fire it at a later point to simulate asynchronicity
    private var callback: Callback? = null

    @Before
    fun setupMockRestCallHelper() {
        // whenever the restCallHelper gets called store the callback in the local variable
        whenever(mockRestCallHelper.callRestEndpoint(any(), any(), any(), anyOrNull())).then {
            callback = (it.arguments[2] as Callback)
            return@then Unit
        }
    }

    // sets the adapter of the RecyclerView in the Activity to the passed TopicAdapter
    private fun setTopicAdapterOfRecyclerView(topicAdapter: TopicAdapter) {
        activityRule.runOnUiThread {
            activityRule.activity?.recyclerView?.adapter = topicAdapter
        }
    }

    // performs the 'asynchronous' firing of the callback
    private fun fireMockRestCallHelperCallback() {
        activityRule.runOnUiThread {
            callback?.onSuccess(
                JSONObject(
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
                )
            )
        }
    }

    @Test
    fun loadsTopicsIntoRecyclerView() {
        val topicAdapter = TopicAdapter(
            activityRule.activity.searchView ?: SearchView(activityRule.activity.applicationContext),
            mockRestCallHelper
        )
        setTopicAdapterOfRecyclerView(topicAdapter)
        // needs to wait for RecyclerView to build layout
        Thread.sleep(100L)
        fireMockRestCallHelperCallback()

        onView(withText("Test title")).check(matches(hasSibling(withId(R.id.topicCardSwitch))))
        onView(withText("Second title")).check(matches(hasSibling(withId(R.id.topicCardSwitch))))
    }

    @Test
    fun setsSubscribeButtonsCorrectly() {
        // mock preferences to create a subscription for topic 2
        val preferences = mock<SharedPreferences>()
        whenever(preferences.getBoolean(any(), any())).thenReturn(false)
        whenever(preferences.getBoolean(eq("topic/2"), any())).thenReturn(true)
        val topicAdapter = TopicAdapter(
            activityRule.activity.searchView
                ?: SearchView(activityRule.activity.applicationContext),
            mockRestCallHelper,
            preferences
        )
        setTopicAdapterOfRecyclerView(topicAdapter)
        // needs to wait for RecyclerView to build layout
        Thread.sleep(100L)
        fireMockRestCallHelperCallback()

        onView(allOf(withId(R.id.topicCardSwitch), hasSibling(withText("Test title"))))
            .check(
                matches(isNotChecked())
            )
        onView(allOf(withId(R.id.topicCardSwitch), hasSibling(withText("Second title"))))
            .check(
                matches(isChecked())
            )
    }

    @Test
    fun queryChangesRecyclerView() {
        val topicAdapter = TopicAdapter(
            activityRule.activity.searchView
                ?: SearchView(activityRule.activity.applicationContext), mockRestCallHelper
        )
        setTopicAdapterOfRecyclerView(topicAdapter)
        // needs to wait for RecyclerView to build layout
        Thread.sleep(100L)
        fireMockRestCallHelperCallback()

        onView(withText("Test title")).check(matches(hasSibling(withId(R.id.topicCardSwitch))))
        onView(withText("Second title")).check(matches(hasSibling(withId(R.id.topicCardSwitch))))

        activityRule.runOnUiThread {
            activityRule.activity?.searchView?.setQuery("second", true)
            topicAdapter.notifyQueryChanged()
        }

        onView(withText("Test title")).check(doesNotExist())
        onView(withText("Second title")).check(matches(hasSibling(withId(R.id.topicCardSwitch))))
    }


}*/