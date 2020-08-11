package de.uulm.automotiveuulmapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.uulm.automotiveuulmapp.locationFavourites.LocationFavouritesFragment
import de.uulm.automotiveuulmapp.topicFragment.TopicFragment

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val navBar = findViewById<BottomNavigationView>(R.id.bottomNavigationBar)
        loadFragment(TopicFragment())

        navBar.setOnNavigationItemSelectedListener{item ->
            var fragment: Fragment? = null
            when(item.itemId) {
                R.id.nav_item_messages -> {
                    //TODO: Add navigation logic to message list activity
                }
                R.id.nav_item_locations -> {
                    //TODO: Add navigation logic to message list activity
                }
                R.id.nav_item_subscriptions -> {
                    fragment = TopicFragment()
                }
            }
            loadFragment(fragment)
        }

        navBar.setOnNavigationItemReselectedListener {
            //do nothing when reselected
        }
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            return true
        }
        return false
    }
}
