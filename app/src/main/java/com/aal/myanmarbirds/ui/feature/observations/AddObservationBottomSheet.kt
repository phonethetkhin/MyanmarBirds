package com.aal.myanmarbirds.ui.feature.observations

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.aal.myanmarbirds.ui.theme.MyanmarBirdPreview
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsColor
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsTypographyTokens
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddObservationBottomSheet(
    birdName: String,
    note: String,
    latitude: Double?,
    longitude: Double?,
    onBirdNameChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onLocationSelected: (Double, Double) -> Unit,
    onCancelClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    val today = LocalDate.now()
    var selectedDate by remember { mutableStateOf(today) }
    var showDatePicker by remember { mutableStateOf(false) }

    val formatter = remember {
        DateTimeFormatter.ofPattern("MMM d, yyyy")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.2f)),
        contentPadding = PaddingValues(bottom = 64.dp)
    ) {

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            SheetHeader(
                onCancelClick = onCancelClick,
                onSaveClick = onSaveClick
            )
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        item {
            Text(
                text = "Add Observation",
                style = MyanmarBirdsTypographyTokens.Header.copy(
                    color = MyanmarBirdsColor.current.black,
                    fontWeight = FontWeight.Black
                ),
                modifier = Modifier.padding(start = 24.dp)
            )
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            SectionCard {
                SectionRow(
                    title = "Date",
                    trailingContent = {
                        Surface(
                            color = MyanmarBirdsColor.current.gray_100,
                            shape = RoundedCornerShape(8.dp),
                            onClick = { showDatePicker = true }
                        ) {
                            Text(
                                text = selectedDate.format(formatter),
                                style = MyanmarBirdsTypographyTokens.Body.copy(
                                    color = MyanmarBirdsColor.current.gray_900,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                )

                HorizontalDivider(
                    color = MyanmarBirdsColor.current.gray_100,
                )
                ObservationTextField(
                    label = "Bird Name",
                    value = birdName,
                    onValueChange = { onBirdNameChange(it) }
                )

                SectionRow(
                    title = "Location",
                    trailingContent = {
                        if (latitude != null && longitude != null) {
                            Text(
                                text = "%.5f, %.5f".format(latitude, longitude),
                                style = MyanmarBirdsTypographyTokens.Body.copy(
                                    color = MyanmarBirdsColor.current.gray_800
                                )
                            )
                        }
                    }
                )

                HorizontalDivider(color = MyanmarBirdsColor.current.gray_100)

                LocationPickerMap(
                    latitude = latitude,
                    longitude = longitude,
                    onLocationSelected = onLocationSelected
                )

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(
                    color = MyanmarBirdsColor.current.gray_100,
                )

                TextButtonRow("Reset to Current Location")

                HorizontalDivider(
                    color = MyanmarBirdsColor.current.gray_100,
                )

                ObservationTextField(
                    label = "Note",
                    value = note,
                    onValueChange = { onNoteChange(it) },
                    minLines = 1,
                    maxLines = 3,
                )

                TextButtonRow("Add Photo")

                BoxPlaceholder(height = 200.dp)

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // ✅ Date Picker Dialog
    if (showDatePicker) {

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun SectionContent() {

    // Date
    SectionRow(
        title = "Date",
        trailingContent = {
            Surface(
                color = MyanmarBirdsColor.current.gray_100,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Aug 29, 2025",
                    style = MyanmarBirdsTypographyTokens.Body.copy(
                        color = MyanmarBirdsColor.current.gray_900,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    )

    HorizontalDivider()

    SectionRow("Bird Name")
    HorizontalDivider()

    SectionRow("Location")
    HorizontalDivider()

    BoxPlaceholder(height = 200.dp)

    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider()

    TextButtonRow("Reset to Current Location")

    HorizontalDivider()

    SectionRow(
        title = "Note",
    )

    HorizontalDivider()

    TextButtonRow("Add Photo")

    BoxPlaceholder(height = 200.dp)

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun SheetHeader(
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Cancel",
            color = MyanmarBirdsColor.current.close_blue,
            style = MyanmarBirdsTypographyTokens.Body
        )

        Text(
            text = "Save",
            color = MyanmarBirdsColor.current.close_blue,
            style = MyanmarBirdsTypographyTokens.Body
        )
    }
}

@Composable
private fun SectionCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(
                color = MyanmarBirdsColor.current.white,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp),
        content = content
    )
}

@Composable
private fun SectionRow(
    title: String,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MyanmarBirdsTypographyTokens.Body.copy(
                color = MyanmarBirdsColor.current.gray_800,
                fontWeight = FontWeight.Bold
            )
        )

        trailingContent?.invoke()
    }
}

@Composable
private fun TextButtonRow(
    text: String
) {
    Text(
        text = text,
        style = MyanmarBirdsTypographyTokens.Body.copy(
            color = MyanmarBirdsColor.current.blue_500,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.padding(vertical = 32.dp)
    )
}

@Composable
private fun BoxPlaceholder(height: Dp) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(MyanmarBirdsColor.current.blue_500)
    ) {}
}

@Composable
fun LocationPickerMap(
    latitude: Double?,
    longitude: Double?,
    onLocationSelected: (Double, Double) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Create MapView only once
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(15.0)
        }
    }

    // Remember marker (only once)
    val marker = remember {
        Marker(mapView).apply {
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }
    }

    // Handle lifecycle properly (VERY IMPORTANT)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> mapView.onDetach()
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Update map when latitude/longitude changes
    LaunchedEffect(latitude, longitude) {

        val lat = latitude ?: 16.8409
        val lng = longitude ?: 96.1735

        val geoPoint = GeoPoint(lat, lng)

        mapView.controller.setCenter(geoPoint)

        marker.position = geoPoint

        if (!mapView.overlays.contains(marker)) {
            mapView.overlays.add(marker)
        }

        mapView.invalidate()
    }

    // Add tap listener only once
    DisposableEffect(Unit) {

        val overlay = object : Overlay() {
            override fun onSingleTapConfirmed(
                e: MotionEvent?,
                mapView: MapView?
            ): Boolean {

                if (e != null && mapView != null) {

                    val projection = mapView.projection
                    val geoPoint = projection.fromPixels(
                        e.x.toInt(),
                        e.y.toInt()
                    ) as GeoPoint

                    marker.position = geoPoint
                    mapView.overlays.clear()
                    mapView.overlays.add(marker)
                    mapView.invalidate()

                    onLocationSelected(
                        geoPoint.latitude,
                        geoPoint.longitude
                    )
                }
                return true
            }
        }

        mapView.overlays.add(overlay)

        onDispose {
            mapView.overlays.remove(overlay)
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Preview
@Composable
private fun AddObservationBottomSheetPreview() {
    MyanmarBirdPreview {
        AddObservationBottomSheet(
            birdName = "",
            note = "",
            onBirdNameChange = {},
            onNoteChange = {},
            latitude = 111.50,
            longitude = 111.80,
            onLocationSelected = {} as (Double, Double) -> Unit,
            onCancelClick = {}
        ) { }
    }
}
