package com.aal.myanmarbirds.ui.feature.observations

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.aal.myanmarbirds.ui.theme.MyanmarBirdPreview
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsColor
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsTypographyTokens
import com.aal.myanmarbirds.util.clickable
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
    var imagePath by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    lateinit var tempImageUri: Uri

    val galleryLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let {
                val savedPath = compressAndSaveImage(context, it)
                imagePath = savedPath
            }
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                val savedPath = compressAndSaveImage(context, tempImageUri)
                imagePath = savedPath
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

                TextButtonRow(text = "Reset to Current Location", verticalPadding = 32.dp)

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
                    Text(
                        text = "Add Photo",
                        style = MyanmarBirdsTypographyTokens.Body.copy(
                            color = MyanmarBirdsColor.current.blue_500,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .padding(vertical = 32.dp)
                            .clickable {
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
                            }
                    )

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
                                imagePath = null
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
            style = MyanmarBirdsTypographyTokens.Body,
            modifier = Modifier.clickable { onCancelClick() }
        )

        Text(
            text = "Save",
            color = MyanmarBirdsColor.current.close_blue,
            style = MyanmarBirdsTypographyTokens.Body,
            modifier = Modifier.clickable { onSaveClick() }
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
) {
    Text(
        text = text,
        style = MyanmarBirdsTypographyTokens.Body.copy(
            color = MyanmarBirdsColor.current.blue_500,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.padding(vertical = verticalPadding)
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
            latitude = 111.50,
            longitude = 111.80,
            onLocationSelected = {} as (Double, Double) -> Unit,
            onCancelClick = {}
        ) { }
    }
}
