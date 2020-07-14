package de.uulm.automotiveuulmapp.welcome

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.SubscribeActivity

/**
 * A simple [Fragment] subclass as the first Welcome App Introduction slider content.
 */
class IntroFragmentWelcome : Fragment(), OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedinstanceState: Bundle?
    ): View? {
        val myView: View = inflater.inflate(R.layout.fragment_intro_welcome, container, false)
        val myButton = myView.findViewById<Button>(R.id.welc1_button1)
        myButton.setOnClickListener(this)
        return myView
    }

    /**
     * defines what is happening when the user presses the "skip" button
     *
     * @param v
     */
    override fun onClick(v: View?) {
        val i = Intent(activity, SubscribeActivity::class.java)
        startActivity(i)
    }
}


