package de.uulm.automotiveuulmapp.messageFragment

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.support.test.InstrumentationRegistry
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import de.uulm.automotiveuulmapp.MessageFragmentTestActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MessageDeletionHintTest {
    @get:Rule
    val intentsTestRule = IntentsTestRule(MessageFragmentTestActivity::class.java, false, false)

    @Before
    fun init(){
        Intents.init()
        setSharedPreferences()
    }

    @After
    fun after(){
        Intents.release()
    }

    @Test
    fun hintDisplayedOnFirstRun(){
        val matcherIntent = hasComponent(MessageDeletionHelper::class.java.name)
        intending(matcherIntent).respondWith(Instrumentation.ActivityResult(
            Activity.RESULT_OK, null))
        intentsTestRule.launchActivity(Intent())
        Intents.intended(matcherIntent)
    }

    private fun setSharedPreferences() {
        val instrumentationContext = InstrumentationRegistry.getContext()

        // targetContext is device specific so should be initiliazed with Instrumentation()
        val targetContext = InstrumentationRegistry.getTargetContext()

        // over targetContext, custom preference file should be open in PRIVATE_MODE to be secure
        // and the preferencesEditor should editable
        val preferencesEditor = targetContext.getSharedPreferences("firstMessage", Context.MODE_PRIVATE).edit()

        // we can clear(), putString(key, value: String)
        // putInt, putLong, putBoolean, ...
        // after function, need to commit() the changes.
        preferencesEditor.clear()
        preferencesEditor.putBoolean("showMessageDeletionHint", true)
        preferencesEditor.commit()
    }
}