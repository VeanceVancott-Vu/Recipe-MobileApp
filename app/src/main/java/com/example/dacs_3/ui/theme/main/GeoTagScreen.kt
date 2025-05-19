package com.example.dacs_3.ui.theme.main

// Imports
// Imports
import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.dacs_3.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow
import java.io.IOException
import java.util.Locale

// Data class for user/location info
data class LocationItem(val name: String, val address: String)

// Geocode a single address to GeoPoint (suspend to call off main thread)
suspend fun geocodeAddress(context: Context, address: String): GeoPoint? {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale("vi", "VN"))
            val addresses = geocoder.getFromLocationName(address, 1)
            if (!addresses.isNullOrEmpty()) {
                val loc = addresses[0]
                GeoPoint(loc.latitude, loc.longitude)
            } else null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}

@Composable
fun GeoTagScreen(locations: List<LocationItem>,navController: NavController) {
    val context = LocalContext.current
    var geoPoints by remember { mutableStateOf<List<Pair<LocationItem, GeoPoint>>>(emptyList()) }

    LaunchedEffect(locations) {
        Configuration.getInstance().load(
            context,
            context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
        )
        val resolvedPoints = locations.mapNotNull { loc ->
            val point = geocodeAddress(context, loc.address)
            if (point != null) loc to point else null
        }
        geoPoints = resolvedPoints
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (geoPoints.isNotEmpty()) {
            AndroidView(
                factory = { ctx ->
                    MapView(ctx).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        controller.setZoom(5.0)

                        val avgLat = geoPoints.map { it.second.latitude }.average()
                        val avgLon = geoPoints.map { it.second.longitude }.average()
                        controller.setCenter(GeoPoint(avgLat, avgLon))

                        geoPoints.forEach { (loc, point) ->
                            val marker = Marker(this).apply {
                                position = point
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                title = loc.name
                                subDescription = loc.address
                                setOnMarkerClickListener { marker, _ ->
                                    marker.showInfoWindow() // this displays the title/subdesc popup
                                    Log.d("MapClick", "User clicked name: ${loc.name}, Address: ${loc.address}")
                                    true
                                }
                            }
                            overlays.add(marker)
                        }

                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(
                "Không tìm thấy vị trí nào.",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}




