package com.aal.myanmarbirds.ui.feature.observations.viewmodel

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

    fun onEvent(event: ObservationScreenEvent) {
        when (event) {
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
    val selectedBodyColor: String = "",
    val isAddObservationBottomSheetOpen: Boolean = false,
) : BaseUiState


sealed class ObservationScreenEvent : BaseUiEvent {
    data object BackPressed : ObservationScreenEvent()
    data class UpdateBodyColor(val color: String) : ObservationScreenEvent()
    data object OpenAddObservationBottomSheet : ObservationScreenEvent()
    data object CloseAddObservationBottomSheet : ObservationScreenEvent()


}




