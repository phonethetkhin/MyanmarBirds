package com.aal.myanmarbirds.ui.feature.observations

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.aal.myanmarbirds.util.debugAddressFields
import com.aal.myanmarbirds.util.getLocationName
import org.maplibre.android.annotations.Marker
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style

@Composable
fun LocationPickerMap(
    selectedLatitude: Double?,
    resetTrigger: Int,
    onReset: () -> Unit,
    currentLatitude: Double?,
    selectedLongitude: Double?,
    currentLongitude: Double?,
    onFetchCurrentLoc: () -> Unit,
    onLocationSelected: (Double, Double) -> Unit = { _, _ -> }
) {

    val context = LocalContext.current

    val mapView = remember {
        MapView(context).apply {
            id = android.view.View.generateViewId()
        }
    }

    var mapRef by remember { mutableStateOf<MapLibreMap?>(null) }
    var markerRef by remember { mutableStateOf<Marker?>(null) }
    var isMapReady by remember { mutableStateOf(false) }


    val mapTilerKey = "UE0iWVkk5q3sPd9VyYbv"
    val selectedLocation =
        LatLng(
            selectedLatitude ?: currentLatitude ?: 16.840,
            selectedLongitude ?: currentLongitude ?: 96.200
        )
    Log.e("testASDF", "iniCurrentLocation, ${selectedLocation}")

    /* ---------------- Map Lifecycle ---------------- */

    DisposableEffect(Unit) {
        try {
            mapView.onCreate(null)
            mapView.onStart()
            mapView.onResume()
        } catch (e: Exception) {
            Log.e("testASDF", "Error in map lifecycle")
        }

        onDispose {
            try {
                mapView.onPause()
                mapView.onStop()
                mapView.onDestroy()
            } catch (e: Exception) {
                Log.e("testASDF", "Error disposing map")
            }
        }
    }

    /* ---------------- Helper Functions ---------------- */

    fun updateMarkerPosition(map: MapLibreMap, newPosition: LatLng) {
        try {
            // Remove old marker if it exists
            markerRef?.let { oldMarker ->
                map.removeMarker(oldMarker)
            }

            // Add new marker
            markerRef = map.addMarker(
                MarkerOptions()
                    .position(newPosition)
                    .title("Bird Found")
            )
            val locationName = getLocationName(context, newPosition.latitude, newPosition.longitude)
            debugAddressFields(context, newPosition.latitude, newPosition.longitude)

            Log.e("testASDF", "Marker updated to: $newPosition")
            Log.e("testASDF", "LocationName: $locationName")
        } catch (e: Exception) {
            Log.e("testASDF", "Error updating marker")
        }
    }

    fun moveToLocation(map: MapLibreMap, location: LatLng, force: Boolean = false) {
        Log.e("testASDF", "moveToLocation, $location, force=$force")

        if (!isMapReady) {
            Log.e("testASDF", "Map not ready, skipping move")
            return
        }

        try {
            // First, set camera position directly to ensure it moves
            map.cameraPosition = CameraPosition.Builder()
                .target(location)
                .zoom(15.0)
                .build()

            // Update marker
            updateMarkerPosition(map, location)

            // Also animate for smoothness
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(location, 15.0),
                500,
                object : MapLibreMap.CancelableCallback {
                    override fun onCancel() {
                        Log.e("testASDF", "Camera animation cancelled")
                    }

                    override fun onFinish() {
                        Log.e("testASDF", "Camera animation finished")
                        onLocationSelected(location.latitude, location.longitude)
                    }
                }
            )

            Log.e("testASDF", "Camera moved successfully to: $location")
        } catch (e: Exception) {
            Log.e("testASDF", "Error moving camera: ${e.message}")
            e.printStackTrace()
        }
    }

    fun initializeMap(map: MapLibreMap) {
        mapRef = map

        map.setStyle(
            Style.Builder()
                .fromUri("https://api.maptiler.com/maps/streets/style.json?key=$mapTilerKey")
        ) { style ->
            isMapReady = true

            // Set camera position
            map.cameraPosition = CameraPosition.Builder()
                .target(selectedLocation)
                .zoom(14.0)
                .build()

            // Clear any existing markers and add initial marker
            map.markers.forEach { marker ->
                map.removeMarker(marker)
            }

            // Add initial marker
            markerRef = map.addMarker(
                MarkerOptions()
                    .position(selectedLocation)
                    .title("Bird Found")
            )

            // Configure UI settings
            map.uiSettings.apply {
                isZoomGesturesEnabled = true
                isScrollGesturesEnabled = true
                isTiltGesturesEnabled = true
                isRotateGesturesEnabled = true
                isCompassEnabled = true
            }

            // Add map click listener
            map.addOnMapClickListener { point ->
                if (isMapReady) {
                    updateMarkerPosition(map, point)
                    onLocationSelected(point.latitude, point.longitude)
                }
                true
            }

            Log.e("testASDF", "Map initialized successfully")
        }
    }

    // Common function to handle reset to current location
    fun resetToCurrentLocation() {
        Log.e("testASDF", "resetToCurrentLocation called")

        if (currentLatitude != null && currentLongitude != null && isMapReady) {
            mapRef?.let { map ->
                val location = LatLng(currentLatitude, currentLongitude)
                Log.e("testASDF", "Moving to current location: $location")
                // Increment trigger to force movement even if coordinates haven't changed
                moveToLocation(map, location, force = true)
            }
        }
    }

    Log.e("testASDF", "currentLat234 $currentLatitude")
    Log.e("testASDF", "currentLat234 $currentLatitude")
    Log.e("testASDF", "selectedLat234 $selectedLatitude")
    Log.e("testASDF", "selectedLng234 $selectedLongitude")

    // Effect to handle current location updates
    // Effect to handle current location updates
    LaunchedEffect(resetTrigger, currentLatitude, currentLongitude) {
        Log.e(
            "testASDF",
            "LaunchedEffect triggered - currentLat: $currentLatitude, currentLng: $currentLongitude, isMapReady: $isMapReady"
        )

        // Call the reset function when the effect triggers
        resetToCurrentLocation()
    }


    /* ---------------- UI ---------------- */

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {

        AndroidView(
            factory = { mapView },
            modifier = Modifier
                .fillMaxSize()
                .pointerInteropFilter { motionEvent ->
                    when (motionEvent.action) {
                        android.view.MotionEvent.ACTION_DOWN -> {
                            mapView.parent?.requestDisallowInterceptTouchEvent(true)
                        }

                        android.view.MotionEvent.ACTION_UP,
                        android.view.MotionEvent.ACTION_CANCEL -> {
                            mapView.parent?.requestDisallowInterceptTouchEvent(false)
                        }
                    }
                    false
                },
            update = { view ->
                // Only initialize if map is not already set up
                if (mapRef == null) {
                    view.getMapAsync { map ->
                        initializeMap(map)
                    }
                }
            }
        )

        /* ---------------- Zoom Buttons ---------------- */
        if (isMapReady) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {
                        mapRef?.animateCamera(CameraUpdateFactory.zoomIn())
                    },
                    modifier = Modifier
                        .background(Color.White, CircleShape)
                        .size(42.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Zoom In")
                }

                IconButton(
                    onClick = {
                        mapRef?.animateCamera(CameraUpdateFactory.zoomOut())
                    },
                    modifier = Modifier
                        .background(Color.White, CircleShape)
                        .size(42.dp)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Zoom Out")
                }
            }

            /* ---------------- My Location Button ---------------- */
            IconButton(
                onClick = {
                    onFetchCurrentLoc()
                    onReset()
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .background(Color.White, CircleShape)
                    .size(42.dp)
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "My Location")
            }
        }
    }
}