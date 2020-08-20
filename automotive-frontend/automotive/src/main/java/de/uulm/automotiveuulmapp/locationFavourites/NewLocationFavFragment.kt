package de.uulm.automotiveuulmapp.locationFavourites

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import de.uulm.automotiveuulmapp.BaseFragment
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.locationFavourites.locationFavData.LocationData
import de.uulm.automotiveuulmapp.locationFavourites.locationFavData.LocationDatabase
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

/**
 * Contains a form to add a new favourite location *
 */
class NewLocationFavFragment : BaseFragment() {
    private lateinit var mContext: Context
    private lateinit var locationDatabase: LocationDatabase
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private var location: Location? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        locationDatabase = LocationDatabase.getDatabaseInstance(context)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
        geocoder = Geocoder(mContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val newLocationView = inflater.inflate(R.layout.new_location_fav_fragment, container, false)
        val nameTextView = newLocationView.findViewById<EditText>(R.id.location_fav_name_textfield)
        val addressTextView = newLocationView.findViewById<EditText>(R.id.location_fav_addr_textfield)
        val getLocationButton = newLocationView.findViewById<ImageButton>(R.id.my_location_button)
        val saveLocationButton = newLocationView.findViewById<ImageButton>(R.id.save_new_location_button)
        val addLocationButton = newLocationView.findViewById<ImageButton>(R.id.add_location_button)
        val newLocationForm = newLocationView.findViewById<ConstraintLayout>(R.id.new_location_form)

        /**
         * Show the form, hide the button
         */
        addLocationButton.setOnClickListener{
            it.visibility = View.GONE
            newLocationForm.visibility = View.VISIBLE
        }

        /**
         * If the button to locate the device is pressed, check if location permissions are existing
         * If not, request the permissions
         * Otherwise get the last known location and put its address into the address field
         */
        getLocationButton.setOnClickListener{
            if (!locationPermissionsGranted()) {
                requestPermissions(arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION),5)
            } else {
                setCurrentLocationAsString(addressTextView)
            }
        }

        // while something gets entered into the address field, clear the last recognized location (location variable)
        addressTextView.doOnTextChanged { _,_,_,_ -> location = null }

        /**
         * If the focus changes from the address field, check if the address is valid and set location variable
         */
        addressTextView.setOnFocusChangeListener{ view, hasFocusChanged ->
            if(!hasFocusChanged && addressTextView.text != null){
                val location = checkLocationString(addressTextView.text.toString(), addressTextView)
                if(location != null){
                    //this.location = location
                    Toast.makeText(mContext, "Entered address is valid!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        /**
         * If the location class variable is set, create a location entity from that data
         * and store it into the LocationDatabase.
         * If not, check the address field for a valid address and (if valid) store it into the database
         * Reset and hide form afterwards
         */
        saveLocationButton.setOnClickListener {
            if(this.location == null){
                this.location = checkLocationString(addressTextView.text.toString(), addressTextView)
            }
            if(location != null){
                val name = nameTextView.text
                val addr = addressTextView.text
                val locationEntity = LocationData(UUID.randomUUID(), name.toString(), addr.toString(), location!!.latitude, location!!.longitude)
                launch{
                    locationDatabase.getLocationDao().insert(locationEntity)
                }
                resetForm(nameTextView, addressTextView, addLocationButton, newLocationForm)
            }
        }
        return newLocationView
    }

    /**
     * Checks with the geocoder, if the passed address String is a valid location
     * If it is, return the location and set the textView content to the recognized address
     *
     * @param address String which should be validated as address
     * @param addressTextView Textview to be populated with correct address
     * @return Location object which has been recognized
     */
    private fun checkLocationString(address: String, addressTextView: EditText): Location?{
        try {
            val locationSearchResult = geocoder.getFromLocationName(address,1)
            if(locationSearchResult.isNotEmpty()){
                val locationAddr = locationSearchResult.first()
                val location = Location("dummyprovider")
                location.latitude = locationAddr.latitude
                location.longitude = locationAddr.longitude
                addressTextView.text = SpannableStringBuilder(locationAddr.getAddressLine(0))
                return location
            } else {
                return null
            }
        } catch (e: IOException) {
            Log.e("NewLocationFragment", "checkLocationString: ", e)
            Toast.makeText(mContext, "Error checking location. Please try again!", Toast.LENGTH_SHORT).show()
            return null
        }
    }

    /**
     * Tries to read the last known location of the device.
     * If available, converts the coordinates to a string, enters it into the passed textView and
     * Otherwise shows error message as toast
     *
     * @param textView The view where the address string should be entered
     */
    private fun setCurrentLocationAsString (textView: TextView) {
        if(locationPermissionsGranted()){
            // ignore error, if statement above checks required permissions
            fusedLocationClient.lastLocation.addOnCompleteListener {
                val location = it.result
                if(location == null) {
                    Toast.makeText(mContext, R.string.location_not_accessible_error, Toast.LENGTH_SHORT).show()
                } else {
                    val locationAddress =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    val locationAddressName = locationAddress[0].getAddressLine(0)
                    textView.text = SpannableStringBuilder(locationAddressName)
                    this.location = location
                }
            }.addOnFailureListener{
                Toast.makeText(mContext, R.string.location_not_accessible_error, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(mContext, R.string.location_not_accessible_error, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * This function wraps the checkup, if either coarse or fine location permissions are available
     *
     * @return If any location permissions are granted
     */
    private fun locationPermissionsGranted() : Boolean{
        return (ActivityCompat.checkSelfPermission(
            mContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            mContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    /**
     * Clears the location variable, the form fields and hides the form.
     * Only displays the add button after clearing
     *
     * @param nameTextView Text view containing the name to clear
     * @param addressTextView Text view containing the address string to clear
     * @param addLocationButton Button to display again
     * @param newLocationForm Form to hide
     */
    private fun resetForm(nameTextView: EditText, addressTextView: EditText, addLocationButton: ImageButton, newLocationForm: ConstraintLayout){
        location = null
        nameTextView.text.clear()
        addressTextView.text.clear()
        addLocationButton.visibility = View.VISIBLE
        newLocationForm.visibility = View.GONE
    }
}