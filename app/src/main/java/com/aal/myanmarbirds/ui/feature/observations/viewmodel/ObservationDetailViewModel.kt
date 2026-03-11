package com.aal.myanmarbirds.ui.feature.observations.viewmodel

import com.aal.myanmarbirds.data.repository.home.ObservationRepository
import com.aal.myanmarbirds.ui.base.BaseUiEvent
import com.aal.myanmarbirds.ui.base.BaseUiState
import com.aal.myanmarbirds.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class ObservationDetailViewModel @Inject constructor(
    private val observationRepo: ObservationRepository
) : BaseViewModel<ObservationDetailScreenState, ObservationDetailScreenEvent>(
    ObservationDetailScreenState()
) {

    fun onEvent(event: ObservationDetailScreenEvent) {
        when (event) {


            else -> sendEvent(event)

        }
    }

}


data class ObservationDetailScreenState(
    val isLoading: Boolean = false,

    ) : BaseUiState


sealed class ObservationDetailScreenEvent : BaseUiEvent {
    data object BackPressed : ObservationDetailScreenEvent()

}




