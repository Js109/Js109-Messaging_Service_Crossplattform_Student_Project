package de.uulm.automotiveuulmapp.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import de.uulm.automotiveuulmapp.R

/**
 *  A simple [Fragment] subclass as the second Welcome App Introduction slider content.
 */
class IntroFragmentChannelInformation : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro_channel_information, container, false)
    }
}
