package com.aal.myanmarbirds.ui.feature.observations

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.maplibre.android.annotations.Marker
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView

@Composable
fun LocationPickerMap(
    latitude: Double?,
    longitude: Double?,
    onLocationSelected: (Double, Double) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val mapView = remember {
        MapView(context).apply {
            onCreate(null)
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(horizontal = 24.dp)
    ) { map ->

        map.getMapAsync { mapboxMap ->

            mapboxMap.setStyle("https://demotiles.maplibre.org/style.json") {

                val lat = latitude ?: 16.8409
                val lng = longitude ?: 96.1735

                val initialPosition = LatLng(lat, lng)

                mapboxMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(initialPosition, 15.0)
                )

                var marker: Marker? = null

                marker = mapboxMap.addMarker(
                    MarkerOptions()
                        .position(initialPosition)
                )

                mapboxMap.addOnMapClickListener { point ->

                    marker?.remove()

                    marker = mapboxMap.addMarker(
                        MarkerOptions().position(point)
                    )

                    onLocationSelected(
                        point.latitude,
                        point.longitude
                    )

                    true
                }
            }
        }
    }
}