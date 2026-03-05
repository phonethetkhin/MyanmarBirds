@file:OptIn(ExperimentalMaterial3Api::class)

package com.aal.myanmarbirds.ui.feature.observations.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.aal.myanmarbirds.ui.base.EventHandler
import com.aal.myanmarbirds.ui.feature.components.BodyColors
import com.aal.myanmarbirds.ui.feature.components.MBTopAppBar
import com.aal.myanmarbirds.ui.feature.components.SegmentedColorSelector
import com.aal.myanmarbirds.ui.feature.observations.AddObservationBottomSheet
import com.aal.myanmarbirds.ui.feature.observations.viewmodel.ObservationScreenEvent
import com.aal.myanmarbirds.ui.feature.observations.viewmodel.ObservationScreenState
import com.aal.myanmarbirds.ui.feature.observations.viewmodel.ObservationViewModel
import com.aal.myanmarbirds.ui.theme.MyanmarBirdPreview
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsColor
import com.aal.myanmarbirds.util.circleClickable
import com.aal.myanmarbirds.util.clickable
import com.aal.myanmarbirds.util.toReadableDate
import kotlinx.coroutines.launch
import java.time.LocalDate


@Composable
fun ObservationScreen(
    observationViewModel: ObservationViewModel = hiltViewModel(),
    onEvent: (ObservationScreenEvent) -> Unit
) {
    val state by observationViewModel.uiState.collectAsState()

    EventHandler(observationViewModel) { event ->
        onEvent(event)
    }

    Scaffold(
        topBar = {
            MBTopAppBar(
                text = "မြန်မာနိုင်ငံရှိငှက်မျိုးစိတ်များ",
                onLightbulbClick = {
                    onEvent(ObservationScreenEvent.BackPressed)
                },
                isHomeScreen = false,
            )
        },
    ) { innerPadding ->
        ObservationScreenContent(
            uiState = state,
            onEvent = observationViewModel::onEvent,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ObservationScreenContent(
    uiState: ObservationScreenState,
    onEvent: (ObservationScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    HandleApplySuccessBottomSheet(
        birdName = uiState.birdName,
        note = uiState.note,
        date = uiState.selectedDate,
        imagePath = uiState.imagePath,
        onBirdNameChange = { onEvent(ObservationScreenEvent.OnBirdNameChange(it)) },
        onNoteChange = { onEvent(ObservationScreenEvent.OnNoteChange(it)) },
        isAddObservationBottomSheetOpen = uiState.isAddObservationBottomSheetOpen,
        onBottomSheetClose = { onEvent(ObservationScreenEvent.CloseAddObservationBottomSheet) },
        onDone = { onEvent(ObservationScreenEvent.CloseAddObservationBottomSheet) },
        latitude = uiState.latitude,
        longitude = uiState.longitude,
        onSaveClick = { onEvent(ObservationScreenEvent.SaveObservation) },
        onDateChange = { onEvent(ObservationScreenEvent.UpdateDate(it)) },
        onImagePathChange = { onEvent(ObservationScreenEvent.UpdateImagePath(it)) },
        selectedBodyColor = uiState.selectedBodyColor,
        onBodyColorChange = { onEvent(ObservationScreenEvent.UpdateBodyColor(it)) }
    )

    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.background(color = Color.White)

        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                tint = MyanmarBirdsColor.current.close_blue,
                contentDescription = "Add Icon",
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 24.dp)
                    .size(30.dp)
                    .circleClickable { onEvent(ObservationScreenEvent.OpenAddObservationBottomSheet) }
            )

            SegmentedColorSelector(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                title = "Observations",
                colors = BodyColors.entries.map { it.display },
                selectedColor = uiState.selectedBodyColorFilter,
                onColorSelected = { onEvent(ObservationScreenEvent.UpdateBodyColorFilter(it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                items(uiState.observations) { observation ->
                    ObservationItem(
                        birdName = observation.birdName,
                        location = "San Francisco",
                        date = observation.date.toReadableDate(),
                        bodyColor = observation.bodyColor
                    )
                }

            }

        }
    }
}


@Composable
fun ObservationItem(
    birdName: String,
    location: String,
    date: String,
    bodyColor: String,
    onClick: () -> Unit = {}
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF4F4F4)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = bodyColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = birdName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = location,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text = date,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Open",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun HandleApplySuccessBottomSheet(
    latitude: Double,
    longitude: Double,
    birdName: String,
    note: String,
    date: LocalDate,
    imagePath: String?,
    selectedBodyColor: String,
    onBodyColorChange: (String) -> Unit,
    onBirdNameChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    isAddObservationBottomSheetOpen: Boolean,
    onBottomSheetClose: () -> Unit,
    onDone: () -> Unit,
    onSaveClick: () -> Unit,
    onDateChange: (LocalDate) -> Unit,
    onImagePathChange: (String?) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    if (isAddObservationBottomSheetOpen) {
        ModalBottomSheet(
            dragHandle = null,
            onDismissRequest = { onBottomSheetClose() },
            sheetState = sheetState,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AddObservationBottomSheet(
                    birdName = birdName,
                    note = note,
                    onBirdNameChange = onBirdNameChange,
                    onNoteChange = onNoteChange,
                    latitude = latitude,
                    longitude = longitude,
                    selectedDate = date,
                    imagePath = imagePath,
                    onLocationSelected = { lat, lng ->

                    },

                    onCancelClick = {
                        scope.launch {
                            sheetState.hide()
                            onBottomSheetClose()
                            onDone()
                        }
                    },
                    onSaveClick = {
                        scope.launch {
                            sheetState.hide()
                            onBottomSheetClose()
                            onDone()
                            onSaveClick()
                        }

                    },
                    onDateChange = { onDateChange(it) },
                    onImagePathChange = { onImagePathChange(it) },
                    selectedBodyColor = selectedBodyColor,
                    onBodyColorChange = { onBodyColorChange(it) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun ObservationScreenContentPreview() {
    MyanmarBirdPreview {
        ObservationScreenContent(
            uiState = ObservationScreenState(),
            onEvent = {})
    }
}

