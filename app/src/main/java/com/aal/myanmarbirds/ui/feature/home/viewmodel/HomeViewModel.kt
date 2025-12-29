package com.aal.myanmarbirds.ui.feature.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.aal.myanmarbirds.R
import com.aal.myanmarbirds.data.model.Bird
import com.aal.myanmarbirds.data.repository.home.HomeRepository
import com.aal.myanmarbirds.ui.base.BaseUiEvent
import com.aal.myanmarbirds.ui.base.BaseUiState
import com.aal.myanmarbirds.ui.base.BaseViewModel
import com.aal.myanmarbirds.ui.feature.components.BodyColors
import com.aal.myanmarbirds.ui.feature.components.HeadColors
import com.aal.myanmarbirds.ui.feature.components.SearchMode
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

val sampleBirds = listOf(
    Bird(
        id = "1",
        name = "မြင်းပျိုးငှက်",
        englishName = "Sunbird",
        japaneseName = "スンバード",
        order = "Passeriformes",
        family = "Nectariniidae",
        scientificName = "Cinnyris spp.",
        description = "A small, brightly colored bird found in Myanmar.",
        body = BodyColors.YELLOW.display,
        head = HeadColors.BLUE.display,
        imageNames = listOf(R.drawable.eurasian_hoopoe_1, R.drawable.eurasian_hoopoe_2),
        audioResId = R.raw.anser_indicus
    ),
    Bird(
        id = "2",
        name = "ကောင်ကောင်ငှက်",
        englishName = "Kingfisher",
        japaneseName = "カワセミ",
        order = "Coraciiformes",
        family = "Alcedinidae",
        scientificName = "Alcedo atthis",
        description = "A small to medium-sized bird known for its bright plumage and fishing skill.",
        body = BodyColors.BLUE.display,
        head = HeadColors.BLUE.display,
        imageNames = listOf(R.drawable.large_billed_crow_1),
        audioResId = R.raw.anser_indicus
    ),
    Bird(
        id = "3",
        name = "ပန်းရောင်ငှက်",
        englishName = "Rosefinch",
        japaneseName = "ウソ",
        order = "Passeriformes",
        family = "Fringillidae",
        scientificName = "Carpodacus spp.",
        description = "A bird species with reddish feathers often seen in Myanmar.",
        body = BodyColors.RED.display,
        head = HeadColors.RED.display,
        imageNames = listOf(R.drawable.large_billed_crow_2),
        audioResId = R.raw.anser_indicus
    ),
    Bird(
        id = "4",
        name = "နက်နက်ငှက်",
        englishName = "Crow",
        japaneseName = "カラス",
        order = "Passeriformes",
        family = "Corvidae",
        scientificName = "Corvus spp.",
        description = "A black bird known for intelligence and adaptability.",
        body = BodyColors.BLACK.display,
        head = HeadColors.BLACK.display,
        imageNames = listOf(R.drawable.large_billed_crow_3),
        audioResId = R.raw.anser_indicus
    ),
    Bird(
        id = "5",
        name = "အဖြူငှက်",
        englishName = "White Bird",
        japaneseName = "白い鳥",
        order = "Columbiformes",
        family = "Columbidae",
        scientificName = "Columba livia",
        description = "A white bird commonly seen in urban areas of Myanmar.",
        body = BodyColors.WHITE.display,
        head = HeadColors.WHITE.display,
        imageNames = listOf(R.drawable.rock_dove_1),
        audioResId = R.raw.anser_indicus
    )
)


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : BaseViewModel<HomeScreenState, HomeScreenEvent>(HomeScreenState()) {

    init {
        loadItems()
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.NavigateToDetail -> sendEvent(event)
            is HomeScreenEvent.ShowMessage -> sendEvent(event)
            is HomeScreenEvent.UpdateSearchText -> updateSearchText(event.text)
            is HomeScreenEvent.UpdateSearchMode -> updateSearchMode(event.mode)
            is HomeScreenEvent.UpdateBodyColor -> updateBodyColor(event.color)
            is HomeScreenEvent.UpdateHeadColor -> updateHeadColor(event.color)
            HomeScreenEvent.ClearFilters -> clearFilters()
            else -> {}
        }
    }

    private fun loadItems() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, error = null) }
            try {
                // load from repo (replace with real repo call)

                updateState { state ->
                    state.copy(
                        isLoading = false,
                        items = sampleBirds,
                        filteredItems = sampleBirds
                    )
                }
            } catch (e: Exception) {
                updateState { it.copy(isLoading = false, error = e.message) }
            }
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


data class HomeScreenState(
    val isLoading: Boolean = false,
    val items: List<Bird> = emptyList(),
    val filteredItems: List<Bird> = emptyList(),
    val error: String? = null,
    val searchMode: SearchMode = SearchMode.NAME,
    val searchText: String = "",
    val selectedBodyColor: String = "",
    val selectedHeadColor: String = ""
) : BaseUiState


sealed class HomeScreenEvent : BaseUiEvent {
    data object BackPressed : HomeScreenEvent()
    data class NavigateToDetail(val birdJson: String) : HomeScreenEvent()
    data class ShowMessage(val message: String) : HomeScreenEvent()

    data class UpdateSearchText(val text: String) : HomeScreenEvent()
    data class UpdateSearchMode(val mode: SearchMode) : HomeScreenEvent()
    data class UpdateBodyColor(val color: String) : HomeScreenEvent()
    data class UpdateHeadColor(val color: String) : HomeScreenEvent()
    data object ClearFilters : HomeScreenEvent()
}




