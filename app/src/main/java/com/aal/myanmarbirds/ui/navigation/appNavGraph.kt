package com.aal.myanmarbirds.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.aal.myanmarbirds.ui.feature.detail.screen.DetailScreen
import com.aal.myanmarbirds.ui.feature.detail.screen.OnboardingScreen
import com.aal.myanmarbirds.ui.feature.detail.viewmodel.DetailScreenEvent
import com.aal.myanmarbirds.ui.feature.home.screen.HomeScreen
import com.aal.myanmarbirds.ui.feature.home.viewmodel.HomeScreenEvent

fun NavGraphBuilder.appNavGraph(
    navigator: Navigator
) {
    addOnBoarding(navigator)
    addHome(navigator)
    addDetail(navigator)

}

fun NavGraphBuilder.addOnBoarding(
    navigator: Navigator
) {
    composable<Destinations.OnBoarding> {
        OnboardingScreen(onCloseClick = {
            navigator.navigateUp()
        })
    }
}

fun NavGraphBuilder.addHome(
    navigator: Navigator
) {
    composable<Destinations.Home> {
        HomeScreen { event ->
            when (event) {
                is HomeScreenEvent.BackPressed -> navigator.navigateUp()

                is HomeScreenEvent.NavigateToOnBoarding -> navigator.navigateToOnBoarding()
                is HomeScreenEvent.NavigateToDetail -> navigator.navigateToDetail(birdJsonString = event.birdJson)

                else -> {}
            }
        }
    }
}

fun NavGraphBuilder.addDetail(
    navigator: Navigator
) {
    composable<Destinations.Detail> {
        val destination = it.toRoute<Destinations.Detail>()

        DetailScreen(birdJsonString = destination.birdJsonString) { event ->
            when (event) {
                is DetailScreenEvent.BackPressed -> navigator.navigateUp()

                else -> {}
            }
        }
    }
}

