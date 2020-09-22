package de.uulm.automotiveuulmapp.locationFavourites.locationQueries

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import de.uulm.automotiveuulmapp.R

/**
 * This class should take the location operations like getting the devices current location and the
 * querying for locations based on an address string.
 *
 * @property mContext application context required to use the location services
 * @property locationQueryFinished Class implementing interface using the implemented function as interface for the string query
 */
class LocationHandler (val mContext: Context, private val locationQueryFinished: LocationQueryFinished){
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
    private val geocoder = Geocoder(mContext)
    private var locationStringQueryTask: LocationStringQueryTask? = null

    /**
     * Tries to read the last known location of the device.
     * If available, converts the coordinates to a string, and executes the callback function with the result
     * Otherwise shows error message as toast
     */
    fun getCurrentLocation (callbackFunction: (Location, String?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnCompleteListener {
            val location = it.result
            if(location == null) {
                Toast.makeText(mContext, R.string.location_not_accessible_error, Toast.LENGTH_SHORT).show()
            } else {
                val locationAddress =
                    geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val locationAddressName = locationAddress[0].getAddressLine(0)
                callbackFunction(location, locationAddressName)
            }
        }.addOnFailureListener{
            Toast.makeText(mContext, R.string.location_not_accessible_error, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Check if there are addresses containing the passed address string
     *
     * @param address String which should be validated as address
     */
    fun getTop5Addresses(address: String){
        if(locationStringQueryTask != null) {
            locationStringQueryTask?.cancel(true)
        }
        locationStringQueryTask = LocationStringQueryTask(mContext, locationQueryFinished)
        locationStringQueryTask?.execute(address)
    }
}