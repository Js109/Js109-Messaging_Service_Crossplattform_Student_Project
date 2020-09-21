package de.uulm.automotiveuulmapp.locationFavourites

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import de.uulm.automotiveuulmapp.BaseFragment
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.locationFavourites.locationFavData.LocationData
import de.uulm.automotiveuulmapp.locationFavourites.locationFavData.LocationDatabase
import de.uulm.automotiveuulmapp.locationFavourites.locationQueries.LocationHandler
import de.uulm.automotiveuulmapp.locationFavourites.locationQueries.LocationQueryFinished
import kotlinx.coroutines.launch
import java.util.*

/**
 * Contains a form to add a new favourite location
 * The location can be retrieved by using the devices current position, or by a search string for the
 * address
 */
class NewLocationFavFragment : LocationQueryFinished, BaseFragment(){
    private lateinit var mContext: Context
    private lateinit var locationDatabase: LocationDatabase
    private var location: Location? = null
    //used for autocomplete of location
    private lateinit var locationAutoSuggestAdapter: LocationAutoSuggestAdapter
    private lateinit var locationHandler: LocationHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        locationDatabase = LocationDatabase.getDatabaseInstance(context)
        locationAutoSuggestAdapter = LocationAutoSuggestAdapter(context, android.R.layout.simple_dropdown_item_1line)
        locationHandler =LocationHandler(mContext,this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val newLocationView = inflater.inflate(R.layout.new_location_fav_fragment, container, false)
        val nameTextView = newLocationView.findViewById<EditText>(R.id.location_fav_name_textfield)
        val getLocationButton = newLocationView.findViewById<ImageButton>(R.id.my_location_button)
        val saveLocationButton = newLocationView.findViewById<ImageButton>(R.id.save_new_location_button)
        val addLocationButton = newLocationView.findViewById<ImageButton>(R.id.add_location_button)
        val newLocationForm = newLocationView.findViewById<ConstraintLayout>(R.id.new_location_form)
        val addressTextView = newLocationView.findViewById<AutoCompleteTextView>(R.id.location_fav_addr_textfield)

        /**
         * Hide form in the beginning
         * If the "+"-button is clicked, show the form, hide the button
         */
        newLocationForm.visibility = View.GONE
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
                locationHandler.getCurrentLocation { location, address ->
                    run {
                        this.location = location
                        addressTextView.text = SpannableStringBuilder(address)
                    }
                }
            }
        }

        addressSearchHandling(addressTextView)

        /**
         * Check if a location name has been entered and a location has been set. If not,
         * show a toast message containing an error description.
         * If the location class variable is set, create a location entity from that data
         * and store it into the LocationDatabase. Afterwards the form is reset
         */
        saveLocationButton.setOnClickListener {
            if(this.location == null){
                Toast.makeText(mContext, R.string.location_fav_location_missing, Toast.LENGTH_SHORT).show()
            } else if(nameTextView.text.toString().isBlank()){
                Toast.makeText(mContext, R.string.location_fav_name_missing, Toast.LENGTH_SHORT).show()
            } else {
                val name = nameTextView.text.toString()
                val addr = addressTextView.text.toString()
                val locationEntity = LocationData(UUID.randomUUID(), name, addr, location!!.latitude, location!!.longitude)
                launch{
                    locationDatabase.getLocationDao().insert(locationEntity)
                }
                resetForm(nameTextView, addressTextView, addLocationButton, newLocationForm)
            }
        }

        return newLocationView
    }

    /**
     * Sets up the listeners of the autocomplete text view (address text field)
     *
     * @param addressTextView View for which the listeners should be added
     */
    private fun addressSearchHandling(addressTextView: AutoCompleteTextView) {
        // while something gets entered into the address field, clear the last recognized location (location variable)
        addressTextView.doOnTextChanged { _,_,_,_ -> location = null }
        // number of characters to be entered before the search should be triggered
        addressTextView.threshold = 2
        addressTextView.setAdapter(locationAutoSuggestAdapter)
        addressTextView.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            val addr = locationAutoSuggestAdapter.getObject(position)
            val l = Location("")
            l.longitude = addr.longitude
            l.latitude = addr.latitude
            location = l
        }
        addressTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* not required */ }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                locationHandler.getTop5Addresses(addressTextView.text.toString())
            }

            override fun afterTextChanged(s: Editable?) { /* not required */ }
        })
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

    override fun onResult(addressList: List<Address>) {
        locationAutoSuggestAdapter.setData(addressList)
        locationAutoSuggestAdapter.notifyDataSetChanged()
    }
}