package de.uulm.automotiveuulmapp.locationFavourites.locationQueries

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.util.Log
import java.io.IOException

class LocationStringQueryTask(val mContext: Context, val locationQueryFinished: LocationQueryFinished) : AsyncTask<String,String,List<Address>>(){
    val geocoder = Geocoder(mContext)

    /**
     * Uses the Geocoder to retrieve address suggestions based on the passed address string
     *
     * @param address Address string addresses should be found for.
     * @return Result address list
     */
    override fun doInBackground(vararg address: String?): List<Address> {
        var result = emptyList<Address>()
        if(!address[0].isNullOrEmpty()){
            try {
                result = geocoder.getFromLocationName(address[0],5)
            } catch (e: IOException) {
                Log.e("LocationHandler", "checkLocationString: ", e)
            }
        }
        return result
    }

    /**
     * Calls the function onResult on the passed LocationQueryFinished object
     * as a callback with the resulting list of addresses
     *
     * @param result Address list found for the passed address string
     */
    override fun onPostExecute(result: List<Address>?) {
        if(result != null) locationQueryFinished.onResult(result)
    }
}