package com.aal.myanmarbirds.ui.feature.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.aal.myanmarbirds.ui.base.EventHandler
import com.aal.myanmarbirds.ui.feature.home.viewmodel.HomeScreenEvent
import com.aal.myanmarbirds.ui.feature.home.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onEvent: (HomeScreenEvent) -> Unit
) {
    val state by homeViewModel.uiState.collectAsState()

    EventHandler(homeViewModel) { event ->
        onEvent(event)  // forward event to NavGraph
    }

    HomeScreenContent(onEvent = homeViewModel::onEvent)
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    onEvent: (HomeScreenEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Hello Compose",
            fontSize = 32.sp,
            color = Color.Black
        )
    }

}
