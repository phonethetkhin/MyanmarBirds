package com.aal.myanmarbirds.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun <S : BaseUiState, E : BaseUiEvent> EventHandler(
    viewModel: BaseViewModel<S, E>,
    onEvent: (E) -> Unit
) {
    val handleEvent by viewModel.event.collectAsState(initial = null)

    LaunchedEffect(key1 = handleEvent) {
        handleEvent?.let { onEvent(it) }
    }
}