package de.uulm.automotiveuulmapp

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import de.uulm.automotiveuulmapp.welcome.WelcomeAppIntro
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 * This Test tests the functionality of the WelcomeAppIntro, especially the redirection on Button
 * click.
 *
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedAndroidWelcomeAppIntroTest {

    @get:Rule
    var activityRule: ActivityTestRule<WelcomeAppIntro> =
        ActivityTestRule(WelcomeAppIntro::class.java)

    /**
     * A Test to check if the redirection to SubscribeActivity is working correctly when
     * clicking the button
     */
    @Test
    fun pressSkipButton() {
        var activityMonitor = getInstrumentation()
            .addMonitor(SubscribeActivity::class.java.getName(), null, false)

        // Simulate click on Skip Button
        Espresso.onView(ViewMatchers.withId(R.id.welc1_button1)).perform(ViewActions.click())

        var targetActivity: SubscribeActivity =
            activityMonitor.waitForActivity() as SubscribeActivity
        assertNotNull("Target Activity is not launched", targetActivity);
    }
}
