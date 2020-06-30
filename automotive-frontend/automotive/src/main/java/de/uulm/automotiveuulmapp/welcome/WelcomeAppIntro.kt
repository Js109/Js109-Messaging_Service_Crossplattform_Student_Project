package de.uulm.automotiveuulmapp.welcome

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroCustomLayoutFragment
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import de.uulm.automotiveuulmapp.MenuActivity
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.TopicFragment

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
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.fragment_welcome1))
        // Welcome Slide 2
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.fragment_welcome2))
        // In this Activity the user can subscribe for channels
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.activity_main))
    }

    // defines what will happen when the user presses "Done"
    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        val i = Intent(this@WelcomeAppIntro, MenuActivity::class.java)
        startActivity(i)
        finish()
    }
}
