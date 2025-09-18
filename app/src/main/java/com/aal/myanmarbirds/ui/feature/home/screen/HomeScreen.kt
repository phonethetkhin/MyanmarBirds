package com.aal.myanmarbirds.ui.feature.home.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.aal.myanmarbirds.R
import com.aal.myanmarbirds.data.model.Bird
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
import com.aal.myanmarbirds.ui.feature.home.viewmodel.HomeViewModel

val sampleBirds = listOf(
    Bird(
        name = "မြင်းပျိုးငှက်",
        englishName = "Sunbird",
        japaneseName = "スンバード",
        body = BodyColors.YELLOW.display,
        head = HeadColors.BLUE.display,
        imageNames = listOf(R.drawable.eurasian_hoopoe_1, R.drawable.eurasian_hoopoe_2)
    ),
    Bird(
        name = "ကောင်ကောင်ငှက်",
        englishName = "Kingfisher",
        japaneseName = "カワセミ",
        body = BodyColors.BLUE.display,
        head = HeadColors.BLUE.display,
        imageNames = listOf(R.drawable.large_billed_crow_1)
    ),
    Bird(
        name = "ပန်းရောင်ငှက်",
        englishName = "Rosefinch",
        japaneseName = "ウソ",
        body = BodyColors.RED.display,
        head = HeadColors.RED.display,
        imageNames = listOf(R.drawable.large_billed_crow_2)
    ),
    Bird(
        name = "နက်နက်ငှက်",
        englishName = "Crow",
        japaneseName = "カラス",
        body = BodyColors.BLACK.display,
        head = HeadColors.BLACK.display,
        imageNames = listOf(R.drawable.large_billed_crow_3)
    ),
    Bird(
        name = "အဖြူငှက်",
        englishName = "White Bird",
        japaneseName = "白い鳥",
        body = BodyColors.WHITE.display,
        head = HeadColors.WHITE.display,
        imageNames = listOf(R.drawable.rock_dove_1)
    )
)


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
        topBar = {
            MBTopAppBar(
                text = "မြန်မာနိုင်ငံရှိငှက်မျိုးစိတ်များ",
                onLightbulbClick = {},
                onMemoClick = {}
            )
        },
        floatingActionButton = {
            MemoButton {
                // handle memo button click
            }
        }) { innerPadding ->
        HomeScreenContent(
            birds = sampleBirds,
            onLightbulbClick = {},
            onMemoClick = {},
            navigateToBirdDetail = {},
            onEvent = homeViewModel::onEvent,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun HomeScreenContent(
    birds: List<Bird>,
    onLightbulbClick: () -> Unit,
    onMemoClick: () -> Unit,
    navigateToBirdDetail: (Bird) -> Unit,
    onEvent: (HomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var isShowingOnboarding by remember { mutableStateOf(false) }
    var selectedSearchMode by remember { mutableStateOf(SearchMode.NAME) }
    var searchText by remember { mutableStateOf("") }
    var selectedBodyColor by remember { mutableStateOf("") }
    var selectedHeadColor by remember { mutableStateOf("") }

    // Filtered list
    val filteredBirds = birds.filter { bird ->
        when (selectedSearchMode) {
            SearchMode.NAME ->
                searchText.isEmpty() ||
                        bird.name.contains(searchText, ignoreCase = true) ||
                        bird.englishName.contains(searchText, ignoreCase = true) ||
                        bird.japaneseName.contains(searchText, ignoreCase = true)

            SearchMode.BODY ->
                selectedBodyColor.isEmpty() || bird.body == selectedBodyColor

            SearchMode.HEAD ->
                selectedHeadColor.isEmpty() || bird.head == selectedHeadColor
        }
    }

    Scaffold(
        topBar = {
            MBTopAppBar(
                text = "မြန်မာနိုင်ငံရှိ ငှက်မျိုးစိတ်များ",
                onLightbulbClick = { isShowingOnboarding = true },
                onMemoClick = onMemoClick
            )
        },
        floatingActionButton = {
            MemoButton(onClick = onMemoClick)
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Spacer(modifier = Modifier.height(20.dp))

            // Search Mode Segmented Picker
            SegmentedSearchModePicker(
                selectedMode = selectedSearchMode,
                onModeSelected = { selectedSearchMode = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Search options
            when (selectedSearchMode) {
                SearchMode.NAME -> {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
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
                        selectedColor = selectedBodyColor,
                        onColorSelected = { selectedBodyColor = it }
                    )
                }

                SearchMode.HEAD -> {
                    HorizontalColorSelector(
                        title = "ဦးခေါင်းပိုင်းအရောင်ကိုရွေးပါ",
                        colors = HeadColors.entries.map { it.display },
                        selectedColor = selectedHeadColor,
                        onColorSelected = { selectedHeadColor = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bird List
            LazyColumn {
                items(filteredBirds) { bird ->
                    BirdListItem(bird = bird, onClick = { navigateToBirdDetail(bird) })
                }
            }
        }
    }

//    if (isShowingOnboarding) {
//        OnboardingDialog(onDismiss = { isShowingOnboarding = false })
//    }
}

