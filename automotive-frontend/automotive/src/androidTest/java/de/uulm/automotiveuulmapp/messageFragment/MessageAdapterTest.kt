package de.uulm.automotiveuulmapp.messageFragment

import android.widget.SearchView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import de.uulm.automotiveuulmapp.MessageAdapterTestActivity
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.messages.messagedb.MessageDao
import de.uulm.automotiveuulmapp.messages.messagedb.MessageEntity
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MessageAdapterTest {
    @get:Rule
    val activityRule = ActivityTestRule(MessageAdapterTestActivity::class.java)

    private var mockMessageDao = mock<MessageDao>()
    private var messageAdapter: MessageAdapter? = null

    @Before
    fun setupRecyclerView() {
        whenever(mockMessageDao.getAll()).thenReturn(
            listOf(
                MessageEntity(1, "Test", "Testtitle", "Message text", null, null, false),
                MessageEntity(2, "Sender", "Title 2", "Content text", null, null, true)
            )
        )
        messageAdapter = MessageAdapter(
            activityRule.activity?.searchView
                ?: SearchView(activityRule.activity.applicationContext),
            mockMessageDao,
            activityRule.activity
        )
        activityRule.runOnUiThread {
            activityRule.activity.recyclerView?.adapter = messageAdapter
        }
        // needs to wait for RecyclerView to build layout
        Thread.sleep(100L)
    }

    @Test
    fun loadsMessagesIntoRecyclerView() {
        onView(withText("Testtitle")).check(matches(hasSibling(withId(R.id.message_content_text))))
        onView(withText("Title 2")).check(matches(hasSibling(withId(R.id.message_content_text))))
    }

    @Test
    fun setsFavouriteButton() {
        onView(
            allOf(
                withId(R.id.message_favourite_checkbox),
                hasSibling(withText("Testtitle"))
            )
        ).check(matches(isNotChecked()))
        onView(
            allOf(
                withId(R.id.message_favourite_checkbox),
                hasSibling(withText("Title 2"))
            )
        ).check(
            matches(
                isChecked()
            )
        )
    }

    @Test
    fun queryChangesRecyclerView() {
        activityRule.runOnUiThread {
            activityRule.activity.searchView?.setQuery("content", true)
            messageAdapter?.notifyQueryChanged()
        }

        onView(withText("Testtitle")).check(doesNotExist())
        onView(withText("Title 2")).check(matches(hasSibling(withId(R.id.message_content_text))))
    }

}