package de.uulm.automotiveuulmapp.messages.messageFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import de.uulm.automotiveuulmapp.MessageFragmentTestActivity
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.messages.messagedb.MessageDao
import de.uulm.automotiveuulmapp.messages.messagedb.MessageDatabase
import de.uulm.automotiveuulmapp.messages.messagedb.MessageEntity
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.hamcrest.CoreMatchers.allOf
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test

class MessageFragmentTest {
    @get:Rule
    val activityRule = ActivityTestRule(MessageFragmentTestActivity::class.java)

    companion object{
        @BeforeClass
        @JvmStatic
        fun setupMessageDao(){
            val mockMessageDao = mockk<MessageDao>()

            mockkObject(MessageDatabase)
            every { MessageDatabase.getDaoInstance(any()) } returns mockMessageDao

            val mockLiveData =  mockk<LiveData<List<MessageEntity>>>()
            var liveDataObserver: Observer<in List<MessageEntity>>? = null

            every { mockLiveData.observeForever(any()) } answers { call ->
                liveDataObserver = (call.invocation.args[0] as Observer<in List<MessageEntity>>)
                liveDataObserver?.onChanged(
                    listOf(
                        MessageEntity(1, "Test", "Testtitle", "Message text", null, null, false, false, null, null, null),
                        MessageEntity(2, "Sender", "Title 2", "Content text", null, null, true, false, null, null, null)
                    )
                ) }
            every { mockMessageDao.getLiveData() } returns mockLiveData
            every { mockMessageDao.delete(any()) } answers {
                liveDataObserver?.onChanged(
                    listOf(
                        MessageEntity(2, "Sender", "Title 2", "Content text", null, null, true, false, null, null, null)
                    )
                )
            }
        }
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
        onView(withId(R.id.messageSearch)).perform(ViewActions.typeText("content"))
        onView(withText("Testtitle")).check(doesNotExist())
        onView(withText("Title 2")).check(matches(hasSibling(withId(R.id.message_content_text))))
    }

    // might require to disable Window animation scale, Transition animation scale, Animation duration scale in
    // Android developer options to be disabled
    @Test
    fun dialogDisplayed(){
        onView(withText("Do you really want to delete this message?")).check(doesNotExist())
        onView(withId(R.id.message_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<MessageAdapter.MessageViewHolder>(0, swipeLeft()))
        onView(withText("Do you really want to delete this message?")).check(matches(isDisplayed()))
    }

    @Test
    fun doNotDelete(){
        dialogDisplayed()
        onView(withText("No")).perform(click())
        onView((withText("Testtitle"))).check(matches(isDisplayed()))
        onView(withText("Do you really want to delete this message?")).check(doesNotExist())
    }

    @Test
    fun doDelete(){
        dialogDisplayed()
        onView(withText("Yes")).perform(click())
        onView((withText("Testtitle"))).check(doesNotExist())
        onView((withText("Title 2"))).check(matches(isDisplayed()))
        onView(withText("Do you really want to delete this message?")).check(doesNotExist())
    }
}