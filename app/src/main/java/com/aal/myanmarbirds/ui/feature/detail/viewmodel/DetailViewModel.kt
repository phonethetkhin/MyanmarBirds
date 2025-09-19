package com.aal.myanmarbirds.ui.feature.detail.viewmodel

import com.aal.myanmarbirds.data.model.Bird
import com.aal.myanmarbirds.data.repository.home.HomeRepository
import com.aal.myanmarbirds.ui.base.BaseUiEvent
import com.aal.myanmarbirds.ui.base.BaseUiState
import com.aal.myanmarbirds.ui.base.BaseViewModel
import com.aal.myanmarbirds.ui.feature.components.SearchMode
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: HomeRepository
) : BaseViewModel<DetailScreenState, DetailScreenEvent>(DetailScreenState()) {

    fun onEvent(event: DetailScreenEvent) {
        when (event) {
            is DetailScreenEvent.NavigateDetail -> sendEvent(event)
            is DetailScreenEvent.ShowMessage -> sendEvent(event)
            is DetailScreenEvent.UpdateSearchText -> updateSearchText(event.text)
            is DetailScreenEvent.UpdateSearchMode -> updateSearchMode(event.mode)
            is DetailScreenEvent.UpdateBodyColor -> updateBodyColor(event.color)
            is DetailScreenEvent.UpdateHeadColor -> updateHeadColor(event.color)
            DetailScreenEvent.ClearFilters -> clearFilters()
            else -> {}
        }
    }

    private fun updateSearchText(text: String) {
        updateState { it.copy(searchText = text) }
        applyFilter()
    }

    private fun updateSearchMode(mode: SearchMode) {
        updateState { it.copy(searchMode = mode) }
        applyFilter()
    }

    private fun updateBodyColor(color: String) {
        updateState { it.copy(selectedBodyColor = color) }
        applyFilter()
    }

    private fun updateHeadColor(color: String) {
        updateState { it.copy(selectedHeadColor = color) }
        applyFilter()
    }

    private fun clearFilters() {
        updateState {
            it.copy(
                searchText = "",
                selectedBodyColor = "",
                selectedHeadColor = "",
                searchMode = SearchMode.NAME
            )
        }
        applyFilter()
    }

    private fun applyFilter() {
        val current = uiState.value
        val birds = current.items

        val filtered = birds.filter { bird ->
            when (current.searchMode) {
                SearchMode.NAME ->
                    current.searchText.isEmpty() ||
                            bird.name.contains(current.searchText, ignoreCase = true) ||
                            bird.englishName.contains(current.searchText, ignoreCase = true) ||
                            bird.japaneseName.contains(current.searchText, ignoreCase = true)

                SearchMode.BODY ->
                    current.selectedBodyColor.isEmpty() || bird.body == current.selectedBodyColor

                SearchMode.HEAD ->
                    current.selectedHeadColor.isEmpty() || bird.head == current.selectedHeadColor
            }
        }

        updateState { it.copy(filteredItems = filtered) }
    }
}


data class DetailScreenState(
    val isLoading: Boolean = false,
    val items: List<Bird> = emptyList(),
    val filteredItems: List<Bird> = emptyList(),
    val error: String? = null,
    val searchMode: SearchMode = SearchMode.NAME,
    val searchText: String = "",
    val selectedBodyColor: String = "",
    val selectedHeadColor: String = ""
) : BaseUiState


sealed class DetailScreenEvent : BaseUiEvent {
    data object BackPressed : DetailScreenEvent()
    data class NavigateDetail(val id: String) : DetailScreenEvent()
    data class ShowMessage(val message: String) : DetailScreenEvent()

    data class UpdateSearchText(val text: String) : DetailScreenEvent()
    data class UpdateSearchMode(val mode: SearchMode) : DetailScreenEvent()
    data class UpdateBodyColor(val color: String) : DetailScreenEvent()
    data class UpdateHeadColor(val color: String) : DetailScreenEvent()
    data object ClearFilters : DetailScreenEvent()
}




