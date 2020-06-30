package de.uulm.automotiveuulmapp.welcome

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroCustomLayoutFragment
import com.github.appintro.AppIntroFragment
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.TopicFragment

class WelcomeAppIntro : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!

        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(
            AppIntroFragment.newInstance(
                title = "Willkommen in Ihrem persönlichen Newskanal",
                description = "Diese Anleitung wird Ihnen helfen sich mit der Anwendung vertraut zu machen."
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "Informationen ohne Ende!",
                description = "Es gibt sogenannte Channels, für die Sie sich registrieren können. Wählen Sie für Sie interessante Channels aus und bekommen nach Belieben Notifications!"
            )
        )
        //addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.activity_main))
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.activity_main))
        addSlide(
            AppIntroFragment.newInstance(
                title = "The title of your slide",
                description = "A description that will be shown on the bottom",
                // imageDrawable = R.drawable.the_central_icon,
                // backgroundDrawable = R.drawable.the_background_image,
                titleColor = Color.YELLOW,
                descriptionColor = Color.RED,
                backgroundColor = Color.BLUE
                // titleTypefaceFontRes = R.font.opensans_regular,
                // descriptionTypefaceFontRes = R.font.opensans_regular,
            )
        )
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
