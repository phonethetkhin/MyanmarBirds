package com.aal.myanmarbirds.ui.feature.observations

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.aal.myanmarbirds.ui.feature.components.BodyColors
import com.aal.myanmarbirds.ui.feature.components.SegmentedColorSelector
import com.aal.myanmarbirds.ui.theme.MyanmarBirdPreview
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsColor
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsTypographyTokens
import com.aal.myanmarbirds.util.clickable
import com.aal.myanmarbirds.util.getLocationName
import java.io.File
import java.io.FileOutputStream
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddObservationBottomSheet(
    birdName: String,
    note: String,
    selectedDate: LocalDate,
    selectedLat: Double?,
    selectedLng: Double?,
    selectedLocName: String?,
    currentLat: Double?,
    currentLng: Double?,
    isFetchingLocation: Boolean,
    imagePath: String?,
    onDateChange: (LocalDate) -> Unit,
    onImagePathChange: (String?) -> Unit,
    onBirdNameChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onFetchCurrentLoc: () -> Unit,
    selectedBodyColor: String,
    onBodyColorChange: (String) -> Unit,
    onLocationSelected: (Double, Double, String) -> Unit,
    onCancelClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    val today = LocalDate.now()
    var showDatePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var resetTrigger by remember { mutableStateOf(0) }

    // States for location permission handling
    var showLocationPermissionRequest by remember { mutableStateOf(false) }
    var showPermissionDeniedDialog by remember { mutableStateOf(false) }
    var showEnableLocationDialog by remember { mutableStateOf(false) }
    var pendingLocationAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    lateinit var tempImageUri: Uri

    // Check if permission is permanently denied
    val shouldShowRationale = remember {
        shouldShowRequestPermissionRationale(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    // Function to check if location is enabled
    fun isLocationEnabled(): Boolean {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            locationManager.isLocationEnabled
        } else {
            @Suppress("DEPRECATION")
            locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
        }
    }
    BackHandler { }

    // Function to open location settings
    fun openLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }

    // Permission launcher with proper handling
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        showLocationPermissionRequest = false
        if (isGranted) {
            // Permission granted, check if location is enabled
            if (isLocationEnabled()) {
                // Location is enabled, execute pending action
                pendingLocationAction?.invoke()
                pendingLocationAction = null
            } else {
                // Location is disabled, show enable location dialog
                showEnableLocationDialog = true
            }
        } else {
            // Permission denied
            if (!shouldShowRationale) {
                // User checked "never ask again" or denied twice
                showPermissionDeniedDialog = true
            } else {
                // First time denial
                Toast.makeText(
                    context,
                    "Location permission is required to get current location",
                    Toast.LENGTH_SHORT
                ).show()
            }
            pendingLocationAction = null
        }
    }

    // Show permission dialog when needed
    if (showLocationPermissionRequest) {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    // Show settings dialog when permission is permanently denied
    if (showPermissionDeniedDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDeniedDialog = false },
            title = { Text("Location Permission Required") },
            text = { Text("Location permission has been permanently denied. Please enable it in app settings to use this feature.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPermissionDeniedDialog = false
                        openAppSettings(context)
                    }
                ) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDeniedDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Show enable location dialog when location is disabled
    if (showEnableLocationDialog) {
        AlertDialog(
            onDismissRequest = { showEnableLocationDialog = false },
            title = { Text("Location is Disabled") },
            text = { Text("Please enable location services to get your current location.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showEnableLocationDialog = false
                        openLocationSettings()
                    }
                ) {
                    Text("Open Location Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showEnableLocationDialog = false
                    // Optionally, you could still execute the action without location
                    // pendingLocationAction?.invoke()
                    // pendingLocationAction = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Function to handle location fetch with permission and location check
    fun handleLocationFetch() {
        if (checkPermission(context)) {
            // Permission already granted, check if location is enabled
            if (isLocationEnabled()) {
                // Location is enabled, fetch location
                onFetchCurrentLoc()
            } else {
                // Location is disabled, show enable location dialog
                showEnableLocationDialog = true
            }
        } else {
            // Need to request permission
            pendingLocationAction = {
                if (isLocationEnabled()) {
                    onFetchCurrentLoc()
                } else {
                    showEnableLocationDialog = true
                }
            }
            showLocationPermissionRequest = true
        }
    }

    val galleryLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let {
                val savedPath = compressAndSaveImage(context, it)
                onImagePathChange(savedPath)
            }
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                val savedPath = compressAndSaveImage(context, tempImageUri)
                onImagePathChange(savedPath)
            }
        }

    fun launchCamera() {
        val file = File.createTempFile(
            "bird_photo_",
            ".jpg",
            context.cacheDir
        )
        tempImageUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        cameraLauncher.launch(tempImageUri)
    }

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                launchCamera()
            } else {
                Toast.makeText(
                    context,
                    "Camera permission is required",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    val galleryPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                galleryLauncher.launch("image/*")
            }
        }

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
                onSaveClick = onSaveClick,
                birdName = birdName,
                selectedBodyColor = selectedBodyColor,
                selectedLat = selectedLat,
                selectedLng = selectedLng,
                imagePath = imagePath
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
                            color = MyanmarBirdsColor.current.blue_500,
                            shape = RoundedCornerShape(8.dp),
                            onClick = { showDatePicker = true }
                        ) {
                            Text(
                                text = selectedDate.format(formatter),
                                style = MyanmarBirdsTypographyTokens.Body.copy(
                                    color = MyanmarBirdsColor.current.white,
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

                SegmentedColorSelector(
                    modifier = Modifier
                        .fillMaxWidth(),
                    title = "Body Color",
                    colors = BodyColors.entries.map { it.display },
                    selectedColor = selectedBodyColor,
                    onColorSelected = {
                        onBodyColorChange(it)
                    }
                )
                HorizontalDivider(
                    color = MyanmarBirdsColor.current.gray_100,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                Column {
                    Text(
                        text = "Location",
                        style = MyanmarBirdsTypographyTokens.Body.copy(
                            color = MyanmarBirdsColor.current.gray_800,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    // Safely display location with fallback values
                    val displayLat = selectedLat ?: currentLat ?: 16.840
                    val displayLng = selectedLng ?: currentLng ?: 96.200
                    val locationName =
                        getLocationName(context, 16.840, 96.200)
                    val displayLocName = selectedLocName ?: locationName

                    Text(
                        text = "$displayLocName\nLat: %.5f, Lng: %.5f".format(
                            displayLat,
                            displayLng
                        ),
                        style = MyanmarBirdsTypographyTokens.Body.copy(
                            color = MyanmarBirdsColor.current.gray_800
                        )
                    )
                }

                HorizontalDivider(
                    color = MyanmarBirdsColor.current.gray_100,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                LocationPickerMap(
                    selectedLatitude = selectedLat,
                    selectedLongitude = selectedLng,
                    resetTrigger = resetTrigger,
                    currentLatitude = currentLat,
                    currentLongitude = currentLng,
                    isFetchingLocation = isFetchingLocation,
                    onFetchCurrentLoc = { handleLocationFetch() },
                    onReset = { resetTrigger++ }
                ) { lat, lng, locName ->
                    onLocationSelected(lat, lng, locName)
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(
                    color = MyanmarBirdsColor.current.gray_100,
                )

                TextButtonRow(
                    text = "Reset to Current Location",
                    verticalPadding = 8.dp,
                    onClick = { handleLocationFetch() }
                )
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

                if (imagePath == null) {

                    // SHOW ADD PHOTO TEXT
                    TextButton(
                        onClick = {
                            showPhotoChooser(
                                context = context,
                                onCamera = {
                                    cameraPermissionLauncher.launch(
                                        Manifest.permission.CAMERA
                                    )
                                },
                                onGallery = {
                                    val permission =
                                        if (Build.VERSION.SDK_INT >= 33)
                                            Manifest.permission.READ_MEDIA_IMAGES
                                        else
                                            Manifest.permission.READ_EXTERNAL_STORAGE

                                    if (ContextCompat.checkSelfPermission(
                                            context,
                                            permission
                                        ) == PackageManager.PERMISSION_GRANTED
                                    ) {
                                        galleryLauncher.launch("image/*")
                                    } else {
                                        galleryPermissionLauncher.launch(permission)
                                    }
                                }
                            )
                        },
                        modifier = Modifier.padding(vertical = 32.dp)
                    ) {
                        Text(
                            text = "Add Photo",
                            style = MyanmarBirdsTypographyTokens.Body.copy(
                                color = MyanmarBirdsColor.current.blue_500,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                } else {

                    // SHOW IMAGE WITH ACTION BUTTONS
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(vertical = 12.dp)
                    ) {

                        Image(
                            painter = rememberAsyncImagePainter(File(imagePath!!)),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )

                        // DELETE
                        IconButton(
                            onClick = {
                                File(imagePath!!).delete()
                                onImagePathChange(null)
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(Color.Black.copy(0.5f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        // REPLACE
                        IconButton(
                            onClick = {
                                showPhotoChooser(
                                    context = context,
                                    onCamera = {
                                        cameraPermissionLauncher.launch(
                                            Manifest.permission.CAMERA
                                        )
                                    },
                                    onGallery = {
                                        val permission =
                                            if (Build.VERSION.SDK_INT >= 33)
                                                Manifest.permission.READ_MEDIA_IMAGES
                                            else
                                                Manifest.permission.READ_EXTERNAL_STORAGE

                                        if (ContextCompat.checkSelfPermission(
                                                context,
                                                permission
                                            ) == PackageManager.PERMISSION_GRANTED
                                        ) {
                                            galleryLauncher.launch("image/*")
                                        } else {
                                            galleryPermissionLauncher.launch(permission)
                                        }
                                    }
                                )
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                                .background(Color.Black.copy(0.5f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Replay,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Button(onClick = { onCancelClick() }) {
                        Text(
                            text = "Cancel",
                            color = MyanmarBirdsColor.current.white,
                            style = MyanmarBirdsTypographyTokens.Title.copy(fontWeight = FontWeight.Bold),
                        )
                    }

                    Button(
                        onClick = {

                            when {
                                birdName.isBlank() -> {
                                    Toast.makeText(
                                        context,
                                        "Bird name is required",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                selectedBodyColor.isBlank() -> {
                                    Toast.makeText(
                                        context,
                                        "Please select body color",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                selectedLat == null || selectedLng == null -> {
                                    Toast.makeText(
                                        context,
                                        "Please select location",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                imagePath == null -> {
                                    Toast.makeText(
                                        context,
                                        "Please add a photo",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                else -> {
                                    onSaveClick()
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "Save",
                            color = MyanmarBirdsColor.current.white,
                            style = MyanmarBirdsTypographyTokens.Title.copy(
                                fontWeight = FontWeight.Bold
                            ),
                        )
                    }
                }
            }
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val todayMillis = LocalDate.now()
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()

                    return utcTimeMillis <= todayMillis
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            onDateChange(
                                Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            )
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

// Add this helper function to check permission
private fun checkPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}

// Helper function to check if we should show rationale
private fun shouldShowRequestPermissionRationale(
    context: Context,
    permission: String
): Boolean {
    return when {
        Build.VERSION.SDK_INT >= 23 -> {
            val activity = context as? androidx.fragment.app.FragmentActivity
            activity?.shouldShowRequestPermissionRationale(permission) ?: false
        }

        else -> false
    }
}

// Helper function to open app settings
private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

fun showPhotoChooser(
    context: Context,
    onCamera: () -> Unit,
    onGallery: () -> Unit
) {
    val options = arrayOf("Camera", "Gallery")

    AlertDialog.Builder(context)
        .setTitle("Select Photo")
        .setItems(options) { _, which ->
            when (which) {
                0 -> onCamera()
                1 -> onGallery()
            }
        }
        .show()
}

@Composable
private fun SheetHeader(
    birdName: String,
    selectedBodyColor: String,
    selectedLat: Double?,
    selectedLng: Double?,
    imagePath: String?,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val context = LocalContext.current
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
            style = MyanmarBirdsTypographyTokens.Title.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.clickable { onCancelClick() }
        )

        Text(
            text = "Save",
            color = MyanmarBirdsColor.current.close_blue,
            style = MyanmarBirdsTypographyTokens.Title.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.clickable {
                when {
                    birdName.isBlank() -> {
                        Toast.makeText(
                            context,
                            "Bird name is required",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    selectedBodyColor.isBlank() -> {
                        Toast.makeText(
                            context,
                            "Please select body color",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    selectedLat == null || selectedLng == null -> {
                        Toast.makeText(
                            context,
                            "Please select location",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    imagePath == null -> {
                        Toast.makeText(
                            context,
                            "Please add a photo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        onSaveClick()
                    }
                }
            }
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
    text: String,
    verticalPadding: Dp,
    onClick: () -> Unit = {}
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.padding(vertical = verticalPadding)
    ) {
        Text(
            text = text,
            style = MyanmarBirdsTypographyTokens.Body.copy(
                color = MyanmarBirdsColor.current.blue_500,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

fun compressAndSaveImage(
    context: Context,
    uri: Uri
): String? {

    val inputStream = context.contentResolver.openInputStream(uri)
        ?: return null

    val bitmap = BitmapFactory.decodeStream(inputStream)
    inputStream.close()

    val maxWidth = 1280
    val ratio = bitmap.width.toFloat() / bitmap.height.toFloat()

    val newWidth: Int
    val newHeight: Int

    if (bitmap.width > maxWidth) {
        newWidth = maxWidth
        newHeight = (maxWidth / ratio).toInt()
    } else {
        newWidth = bitmap.width
        newHeight = bitmap.height
    }

    val resizedBitmap = Bitmap.createScaledBitmap(
        bitmap,
        newWidth,
        newHeight,
        true
    )

    val file = File(
        context.filesDir,
        "bird_${System.currentTimeMillis()}.jpg"
    )

    val outputStream = FileOutputStream(file)

    resizedBitmap.compress(
        Bitmap.CompressFormat.JPEG,
        80, // compression quality
        outputStream
    )

    outputStream.flush()
    outputStream.close()

    bitmap.recycle()
    resizedBitmap.recycle()

    return file.absolutePath
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
            onLocationSelected = { _, _, _ -> },
            onCancelClick = {},
            selectedDate = LocalDate.now(),
            imagePath = null,
            onDateChange = {},
            onImagePathChange = {},
            selectedBodyColor = "",
            onBodyColorChange = {},
            selectedLat = null,
            selectedLng = null,
            currentLat = null,
            currentLng = null,
            onFetchCurrentLoc = {},
            selectedLocName = "",
            isFetchingLocation = false
        )
    }
}