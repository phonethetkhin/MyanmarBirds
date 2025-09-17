package com.aal.myanmarbirds.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aal.myanmarbirds.ui.feature.home.screen.HomeScreen
import com.aal.myanmarbirds.ui.feature.home.viewmodel.HomeScreenEvent

fun NavGraphBuilder.appNavGraph(
    navigator: Navigator
) {
    addHome(navigator)

}

fun NavGraphBuilder.addHome(
    navigator: Navigator
) {
    composable<Destinations.Home> {
        HomeScreen { event ->
            when (event) {
                is HomeScreenEvent.NavigateDetail -> navigator.navigateUp()
                is HomeScreenEvent.ShowMessage -> {
                    // Show snackbar, etc
                }

                else -> {}
            }
        }
    }
}
