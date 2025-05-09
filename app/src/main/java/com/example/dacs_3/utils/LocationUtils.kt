package com.example.dacs_3.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.content.pm.PackageManager
import android.widget.Toast
import java.util.*

fun askForLocationPermission(context: Context, onLocationFetched: (String?) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
    ) {
        Toast.makeText(context, "Ứng dụng cần quyền truy cập vị trí để hoàn tất", Toast.LENGTH_LONG).show()
        onLocationFetched(null)
        return
    }

    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val adminArea = address.adminArea ?: ""
                    val country = address.countryName ?: ""
                    val fullLocation = if (adminArea.isNotEmpty()) "$adminArea, $country" else country
                    onLocationFetched(fullLocation)
                } else {
                    onLocationFetched(null)
                }
            } catch (e: Exception) {
                Log.e("Geocoder", "Failed to get address", e)
                onLocationFetched(null)
            }
        } else {
            onLocationFetched(null)
        }
    }.addOnFailureListener {
        Log.e("Location", "Failed to get location", it)
        onLocationFetched(null)
    }
}
