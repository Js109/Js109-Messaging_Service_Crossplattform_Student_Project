package de.uulm.automotiveuulmapp

import android.app.Instrumentation
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep

/**
 * Instrumented test, which will execute on an Android device.
 * This Test tests the functionality of the MainActivity, especially the redirection.
 * The redirection to WelcomeAppIntroduction cannot be tested because SharedPreferences Permissions
 * are not compatible from API version 29 on.
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedAndroidIntroActivityTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = getInstrumentation().targetContext
        assertEquals("de.uulm.automotiveuulmapp", appContext.packageName)
    }

    @get:Rule
    var activityRuleIntro: ActivityTestRule<IntroActivity> =
        ActivityTestRule(IntroActivity::class.java)

    /**
     * A Test to check if the redirection to SubscribeActivity is working correctly at the
     * second startup
     */
    @Test
    fun introNotStartingAtSecondStartup() {
        sleep(5_000)
        val activityMonitor: Instrumentation.ActivityMonitor = getInstrumentation()
            .addMonitor(MainActivity::class.java.getName(), null, false)

        val targetActivity: MainActivity =
            activityMonitor.waitForActivity() as MainActivity

        assertNotNull("Target Activity is not launched", targetActivity)
    }
}
