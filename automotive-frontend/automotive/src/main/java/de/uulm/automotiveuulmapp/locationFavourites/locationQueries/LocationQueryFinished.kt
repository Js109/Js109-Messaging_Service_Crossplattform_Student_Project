package de.uulm.automotiveuulmapp.locationFavourites.locationQueries

import android.location.Address

interface LocationQueryFinished {
    fun onResult(addressList: List<Address>)
}