package de.uulm.automotiveuulmapp.locationFavourites

import android.content.Context
import android.location.Address
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable

class LocationAutoSuggestAdapter(
    context: Context,
    resource: Int
): ArrayAdapter<String>(context, resource), Filterable {
    private var locations: List<Address> = emptyList()

    fun setData(locationList: List<Address>){
        locations = locationList
    }

    override fun getCount(): Int {
        return locations.count()
    }

    override fun getItem(position: Int): String? {
        return locations[position].getAddressLine(0)
    }

    fun getObject(position: Int): Address{
        return locations[position]
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                val filterResults = FilterResults()
                if (constraint != null) {
                    filterResults.values = locations
                    filterResults.count = locations.size
                }
                return filterResults
            }

            override fun publishResults(
                constraint: CharSequence?,
                results: FilterResults?
            ) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }
}