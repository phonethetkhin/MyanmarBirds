@file:OptIn(ExperimentalMaterial3Api::class)

package com.aal.myanmarbirds.ui.feature.observations.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.launch


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
        isAddObservationBottomSheetOpen = uiState.isAddObservationBottomSheetOpen,
        onBottomSheetClose = { onEvent(ObservationScreenEvent.CloseAddObservationBottomSheet) },
        onDone = { }
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
                title = "Observations",
                colors = BodyColors.entries.map { it.display },
                selectedColor = uiState.selectedBodyColor,
                onColorSelected = { onEvent(ObservationScreenEvent.UpdateBodyColor(it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))


        }
    }
}

@Composable
fun HandleApplySuccessBottomSheet(
    isAddObservationBottomSheetOpen: Boolean,
    onBottomSheetClose: () -> Unit,
    onDone: () -> Unit
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
                    .fillMaxHeight(0.95F)
                    .fillMaxWidth()
            ) {
                AddObservationBottomSheet {
                    scope.launch {
                        sheetState.hide()
                        onBottomSheetClose()
                        onDone()
                    }
                }
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

