package com.aal.myanmarbirds.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : BaseUiState, E : BaseUiEvent>(
    initialState: S
) : ViewModel() {

    // State (observable in Compose)
    protected val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    // One-time Events (snackbar, navigation, etc.)
    private val _event = MutableSharedFlow<E>()
    val event: SharedFlow<E> = _event.asSharedFlow()

    // Update state with reducer
    protected fun updateState(reducer: (S) -> S) {
        _uiState.update(reducer)
    }

    // Send event (consumed once)
    protected fun sendEvent(event: E) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }
}