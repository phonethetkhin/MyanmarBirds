package com.aal.myanmarbirds.ui.feature.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.aal.myanmarbirds.ui.base.EventHandler
import com.aal.myanmarbirds.ui.feature.components.BirdListItem
import com.aal.myanmarbirds.ui.feature.components.BodyColors
import com.aal.myanmarbirds.ui.feature.components.HeadColors
import com.aal.myanmarbirds.ui.feature.components.HorizontalColorSelector
import com.aal.myanmarbirds.ui.feature.components.MBTopAppBar
import com.aal.myanmarbirds.ui.feature.components.MemoButton
import com.aal.myanmarbirds.ui.feature.components.SearchMode
import com.aal.myanmarbirds.ui.feature.components.SegmentedSearchModePicker
import com.aal.myanmarbirds.ui.feature.home.viewmodel.HomeScreenEvent
import com.aal.myanmarbirds.ui.feature.home.viewmodel.HomeScreenState
import com.aal.myanmarbirds.ui.feature.home.viewmodel.HomeViewModel
import com.aal.myanmarbirds.ui.theme.MyanmarBirdPreview
import com.google.gson.Gson


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onEvent: (HomeScreenEvent) -> Unit
) {
    val state by homeViewModel.uiState.collectAsState()

    EventHandler(homeViewModel) { event ->
        onEvent(event)
    }

    Scaffold(
        containerColor = Color.Gray.copy(0.2f),
        topBar = {
            MBTopAppBar(
                text = "မြန်မာနိုင်ငံရှိငှက်မျိုးစိတ်များ",
                onLightbulbClick = {
                    onEvent(HomeScreenEvent.NavigateToOnBoarding)
                },
                isHomeScreen = true,
            )
        },
        floatingActionButton = {
            MemoButton {
                // handle memo button click
            }
        }) { innerPadding ->
        HomeScreenContent(
            state = state,
            onEvent = homeViewModel::onEvent,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun HomeScreenContent(
    state: HomeScreenState,
    onEvent: (HomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var isShowingOnboarding by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.background(color = Color.White)

        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Search Mode Segmented Picker
            SegmentedSearchModePicker(
                selectedMode = state.searchMode,
                onModeSelected = { onEvent(HomeScreenEvent.UpdateSearchMode(it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Search options
            when (state.searchMode) {
                SearchMode.NAME -> {
                    OutlinedTextField(
                        value = state.searchText,
                        onValueChange = { onEvent(HomeScreenEvent.UpdateSearchText(it)) },
                        placeholder = { Text("အမည်နှင့်ရှာဖွေပါ🔍") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }

                SearchMode.BODY -> {
                    HorizontalColorSelector(
                        title = "ခန္ဓာကိုယ်အရောင်ကိုရွေးပါ။",
                        colors = BodyColors.entries.map { it.display },
                        selectedColor = state.selectedBodyColor,
                        onColorSelected = { onEvent(HomeScreenEvent.UpdateBodyColor(it)) }
                    )
                }

                SearchMode.HEAD -> {
                    HorizontalColorSelector(
                        title = "ဦးခေါင်းပိုင်းအရောင်ကိုရွေးပါ",
                        colors = HeadColors.entries.map { it.display },
                        selectedColor = state.selectedHeadColor,
                        onColorSelected = { onEvent(HomeScreenEvent.UpdateHeadColor(it)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


        }
        if (state.filteredItems.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .clip(RoundedCornerShape(8.dp))   // first clip
                    .background(Color.White),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(state.filteredItems) { bird ->
                    BirdListItem(bird = bird, onClick = {
                        val gson = Gson()
                        val birdJson = gson.toJson(bird)

                        onEvent(
                            HomeScreenEvent.NavigateToDetail(
                                birdJson
                            )
                        )
                    })
                    Spacer(modifier = Modifier.height(16.dp))

                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("မည်သည့် ငှက်မျိုးစိတ်မှ မတွေ့ပါ 😢")
            }
        }
    }

//    if (isShowingOnboarding) {
//        OnboardingDialog(onDismiss = { isShowingOnboarding = false })
//    }
}

@Preview
@Composable
private fun HomeScreenContentPreview() {
    MyanmarBirdPreview {
        HomeScreenContent(
            state = HomeScreenState(),
            onEvent = {})
    }
}

