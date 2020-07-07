package de.uulm.automotiveuulmapp.welcome

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroCustomLayoutFragment
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.SubscribeActivity

/**
 * This is the WelcomeAppIntro which is the Introduction for the user
 * when he starts the app for the first time
 */
class WelcomeAppIntro : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Don't call setContentView here!

        // sets Wizardmode to true and because of that the user sees forward button instead of skip
        this.isWizardMode = true
        // Welcome Slide
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.fragment_intro_welcome))
        // Welcome Slide 2
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.fragment_intro_channel_information))
        // Welcome Slide 3
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.fragment_intro_subscription_explanation))
    }

    /**
     * clickSkipMethod defines what happens when the user presses "skip" in the IntroFragmentWelcome
     * @param v
     */
    fun clickSkipMethod() {
        val i = Intent(this@WelcomeAppIntro, SubscribeActivity::class.java)
        startActivity(i)
    }
    // defines what will happen when the user presses "Done"
    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        val i = Intent(this@WelcomeAppIntro, SubscribeActivity::class.java)
        startActivity(i)
        finish()
    }
}
