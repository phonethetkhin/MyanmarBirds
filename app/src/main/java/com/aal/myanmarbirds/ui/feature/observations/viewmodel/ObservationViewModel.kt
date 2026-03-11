package com.aal.myanmarbirds.ui.feature.observations.viewmodel

import androidx.lifecycle.viewModelScope
import com.aal.myanmarbirds.data.repository.home.ObservationRepository
import com.aal.myanmarbirds.db.entities.ObservationEntity
import com.aal.myanmarbirds.ui.base.BaseUiEvent
import com.aal.myanmarbirds.ui.base.BaseUiState
import com.aal.myanmarbirds.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

@HiltViewModel
class ObservationViewModel @Inject constructor(
    private val observationRepo: ObservationRepository,
) : BaseViewModel<ObservationScreenState, ObservationScreenEvent>(ObservationScreenState()) {

    init {
        observeObservations()
    }

    private fun observeObservations() {
        viewModelScope.launch {
            observationRepo.getAllObservations()
                .collect { list ->
                    updateState { it.copy(observations = list) }
                }
        }
    }

    fun updateCurrentLocation(latitude: Double, longitude: Double) {
        updateState { currentState ->
            currentState.copy(
                currentLatitude = latitude,
                currentLongitude = longitude
            )
        }
    }

    fun onEvent(event: ObservationScreenEvent) {
        when (event) {
            is ObservationScreenEvent.OnBirdNameChange -> updateState { it.copy(birdName = event.birdName) }
            is ObservationScreenEvent.OnNoteChange -> updateState { it.copy(note = event.note) }
            is ObservationScreenEvent.UpdateBodyColorFilter -> updateState {
                it.copy(selectedBodyColorFilter = event.color)
            }

            is ObservationScreenEvent.OnLocationSelected -> updateState {
                it.copy(
                    selectedLatitude = event.lat,
                    selectedLongitude = event.lng
                )
            }

            is ObservationScreenEvent.UpdateBodyColor -> updateState {
                it.copy(selectedBodyColor = event.color)
            }

            is ObservationScreenEvent.SaveObservation -> saveObservation()
            is ObservationScreenEvent.OpenAddObservationBottomSheet -> updateState {
                it.copy(
                    isAddObservationBottomSheetOpen = true,
                )
            }

            is ObservationScreenEvent.UpdateDate ->
                updateState { it.copy(selectedDate = event.date) }

            is ObservationScreenEvent.UpdateImagePath ->
                updateState { it.copy(imagePath = event.path) }

            is ObservationScreenEvent.CloseAddObservationBottomSheet -> updateState {
                it.copy(
                    isAddObservationBottomSheetOpen = false,
                )
            }

            else -> sendEvent(event)

        }
    }

    fun saveObservation(

    ) {
        val state = uiState.value

        viewModelScope.launch {

            val observation = ObservationEntity(
                birdName = state.birdName,
                note = state.note,
                date = state.selectedDate
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli(),
                latitude = state.selectedLatitude,
                longitude = state.selectedLongitude,
                imagePath = state.imagePath,
                bodyColor = state.selectedBodyColor
            )

            observationRepo.insertObservation(observation)
        }
    }


}


data class ObservationScreenState(
    val isLoading: Boolean = false,
    val birdName: String = "",
    val selectedLatitude: Double? = null,
    val selectedLongitude: Double? = null,
    val currentLatitude: Double? = null,
    val currentLongitude: Double? = null,
    val note: String = "",
    val selectedBodyColor: String = "",
    val selectedBodyColorFilter: String = "",
    val selectedDate: LocalDate = LocalDate.now(),
    val imagePath: String? = null,
    val observations: List<ObservationEntity> = emptyList(),
    val isAddObservationBottomSheetOpen: Boolean = false,
) : BaseUiState


sealed class ObservationScreenEvent : BaseUiEvent {
    data object BackPressed : ObservationScreenEvent()
    data class UpdateBodyColor(val color: String) : ObservationScreenEvent()
    data class UpdateBodyColorFilter(val color: String) : ObservationScreenEvent()
    data object SaveObservation : ObservationScreenEvent()
    data class OnBirdNameChange(val birdName: String) : ObservationScreenEvent()
    data class OnNoteChange(val note: String) : ObservationScreenEvent()
    data class OnLocationSelected(val lat: Double, val lng: Double) : ObservationScreenEvent()
    data object OpenAddObservationBottomSheet : ObservationScreenEvent()
    data object CloseAddObservationBottomSheet : ObservationScreenEvent()
    data class UpdateDate(val date: LocalDate) : ObservationScreenEvent()
    data class UpdateImagePath(val path: String?) : ObservationScreenEvent()
    data class NavigateToObservationDetail(val observationJson: String) : ObservationScreenEvent()


}




