package com.aal.myanmarbirds.ui.feature.observations.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.aal.myanmarbirds.data.repository.home.HomeRepository
import com.aal.myanmarbirds.ui.base.BaseUiEvent
import com.aal.myanmarbirds.ui.base.BaseUiState
import com.aal.myanmarbirds.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class ObservationViewModel @Inject constructor(
    private val repository: HomeRepository
) : BaseViewModel<ObservationScreenState, ObservationScreenEvent>(ObservationScreenState()) {
    var latitude by mutableStateOf<Double?>(null)
        private set

    var longitude by mutableStateOf<Double?>(null)
        private set

    fun updateLocation(lat: Double, lng: Double) {
        latitude = lat
        longitude = lng
    }

    fun onEvent(event: ObservationScreenEvent) {
        when (event) {
            is ObservationScreenEvent.OnBirdNameChange -> updateState { it.copy(birdName = event.birdName) }
            is ObservationScreenEvent.OnNoteChange -> updateState { it.copy(note = event.note) }
            is ObservationScreenEvent.UpdateBodyColor -> updateBodyColor(event.color)
            is ObservationScreenEvent.OpenAddObservationBottomSheet -> updateState {
                it.copy(
                    isAddObservationBottomSheetOpen = true,
                )
            }

            is ObservationScreenEvent.CloseAddObservationBottomSheet -> updateState {
                it.copy(
                    isAddObservationBottomSheetOpen = false,
                )
            }

            else -> sendEvent(event)

        }
    }

    private fun updateBodyColor(color: String) {
        updateState { it.copy(selectedBodyColor = color) }
    }
}


data class ObservationScreenState(
    val isLoading: Boolean = false,
    val birdName: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val note: String = "",
    val selectedBodyColor: String = "",
    val isAddObservationBottomSheetOpen: Boolean = false,
) : BaseUiState


sealed class ObservationScreenEvent : BaseUiEvent {
    data object BackPressed : ObservationScreenEvent()
    data class UpdateBodyColor(val color: String) : ObservationScreenEvent()
    data class OnBirdNameChange(val birdName: String) : ObservationScreenEvent()
    data class OnNoteChange(val note: String) : ObservationScreenEvent()
    data object OpenAddObservationBottomSheet : ObservationScreenEvent()
    data object CloseAddObservationBottomSheet : ObservationScreenEvent()


}




