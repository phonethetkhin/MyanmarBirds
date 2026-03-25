package com.aal.myanmarbirds.util

import android.content.Context
import android.location.Geocoder
import android.util.Log
import java.util.Locale

fun getLocationName(context: Context, latitude: Double, longitude: Double): String {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)

        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]

            // getAddressLine(0) usually contains the most complete formatted address
            address.getAddressLine(0)?.takeIf { it.isNotBlank() } ?: run {
                // Fallback: build from components if getAddressLine(0) is empty
                val parts = mutableListOf<String>()

                // Add street address (building number + street)
                (address.subThoroughfare?.takeIf { it.isNotBlank() })?.let { parts.add(it) }
                (address.thoroughfare?.takeIf { it.isNotBlank() })?.let { parts.add(it) }

                // Add city/locality
                address.locality?.takeIf { it.isNotBlank() }?.let { parts.add(it) }

                // Add country if needed
                // address.countryName?.takeIf { it.isNotBlank() }?.let { parts.add(it) }

                parts.joinToString(" ")
            } ?: "Unknown Location"
        } else {
            "Unknown Location"
        }
    } catch (e: Exception) {
        e.printStackTrace()
        "Unknown Location"
    }
}
