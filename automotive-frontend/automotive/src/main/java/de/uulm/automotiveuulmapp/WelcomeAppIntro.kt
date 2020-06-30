package de.uulm.automotiveuulmapp

import android.graphics.Color
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.activity_my_custom_app_intro.*

class WelcomeAppIntro : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!

        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(
            AppIntroFragment.newInstance(
            title = "Welcome...",
            description = "This is the first slide of the example"
        ))
        // addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.activity_main
         // ))
        addSlide(AppIntroFragment.newInstance(
            title = "The title of your slide",
            description = "A description that will be shown on the bottom",
            // imageDrawable = R.drawable.the_central_icon,
            // backgroundDrawable = R.drawable.the_background_image,
            titleColor = Color.YELLOW,
            descriptionColor = Color.RED,
            backgroundColor = Color.BLUE
            // titleTypefaceFontRes = R.font.opensans_regular,
            // descriptionTypefaceFontRes = R.font.opensans_regular,
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "...Let's get started!",
            description = "This is the last slide, I won't annoy you more :)"
        ))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        finish()
    }
}
