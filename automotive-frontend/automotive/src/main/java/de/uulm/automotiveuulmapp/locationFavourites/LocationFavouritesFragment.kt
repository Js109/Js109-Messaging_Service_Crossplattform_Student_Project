package de.uulm.automotiveuulmapp.locationFavourites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.uulm.automotiveuulmapp.BaseFragment
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.locationFavourites.locationFavData.LocationData
import de.uulm.automotiveuulmapp.locationFavourites.locationFavData.LocationDatabase
import kotlinx.coroutines.launch

/**
 * Displays a list of previously created location favourites
 * as well as a form to add new locations
 */
class LocationFavouritesFragment : BaseFragment() {
    private lateinit var mContext: Context
    private lateinit var locationDatabase: LocationDatabase

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        locationDatabase = LocationDatabase.getDatabaseInstance(mContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_location_favourites, container, false)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.locations_recycler_view)
        val adapter = LocationFavouriteRecyclerAdapter(mContext, this)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(mContext)
        locationDatabase.getLocationDao().getAll().observe(viewLifecycleOwner, Observer {locations ->
            adapter.changeLocations(locations)
        })
        return view
    }

    fun deleteLocation(location:LocationData){
        launch {
            locationDatabase.getLocationDao().delete(location)
        }
    }
}
