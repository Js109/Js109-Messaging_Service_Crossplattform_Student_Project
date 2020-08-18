package de.uulm.automotiveuulmapp.locationFavourites

import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import de.uulm.automotiveuulmapp.BaseFragment
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.locationFavourites.locationFavData.LocationData
import de.uulm.automotiveuulmapp.locationFavourites.locationFavData.LocationDatabase

class LocationFavouriteRecyclerAdapter (var mContext: Context, val fragment: LocationFavouritesFragment) :
    RecyclerView.Adapter<LocationFavouriteRecyclerAdapter.ViewHolder>() {
    private var locations: List<LocationData> = emptyList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameView: TextView = itemView.findViewById(R.id.location_name_text_field)
        var addressView: TextView = itemView.findViewById(R.id.location_address_text_field)
        val deleteButtonView: ImageButton = itemView.findViewById(R.id.delete_location_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.location_favourite, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameView.text = locations[position].name
        holder.addressView.text = locations[position].address
        holder.deleteButtonView.setOnClickListener{
            fragment.deleteLocation(locations[position])
            notifyItemRemoved(position)
        }
    }

    /**
     * Updates the displayed location list
     *
     * @param locations New location list
     */
    fun changeLocations(locations: List<LocationData>){
        this.locations = locations
        notifyDataSetChanged()
    }
}