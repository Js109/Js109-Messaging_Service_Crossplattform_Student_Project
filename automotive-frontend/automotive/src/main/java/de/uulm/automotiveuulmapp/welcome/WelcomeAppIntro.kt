package de.uulm.automotiveuulmapp.welcome

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import de.uulm.automotiveuulmapp.MainActivity

/**
 * This is the WelcomeAppIntro which is the Introduction for the user
 * when he starts the app for the first time
 */
class WelcomeAppIntro : AppIntro()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Don't call setContentView here!

        // sets Wizardmode to true and because of that the user sees forward button instead of skip
        this.isWizardMode = true
        // Welcome Slide
        addSlide(fragment = IntroFragmentWelcome())
        // Welcome Slide Channel Information
        addSlide(fragment = IntroFragmentChannelInformation())
        // Welcome Slide Subscription Explanation
        addSlide(fragment = IntroFragmentSubscriptionExplanation())
        // Welcome Slide Notification Explanation
        addSlide(IntroFragmentNotificationExplanation())
        // Welcome Slide Message History Explanation
        addSlide(IntroFragmentMessageHistoryExplanation())
        // Welcome Slide Location Favourite Explanation
        addSlide(IntroFragmentLocationFavouriteExplanation())
    }

    // defines what will happen when the user presses "Done"
    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        val i = Intent(this@WelcomeAppIntro, MainActivity::class.java)
        startActivity(i)
        finish()
    }
}
