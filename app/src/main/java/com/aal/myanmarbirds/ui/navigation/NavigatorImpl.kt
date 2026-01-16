package com.aal.myanmarbirds.ui.navigation

import androidx.navigation.NavController

class NavigatorImpl(private val navController: NavController) : Navigator {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun navigateToOnBoarding() {
        navController.navigate(Destinations.OnBoarding)
    }

    override fun navigateToObservation() {
        navController.navigate(Destinations.Observation)
    }

    override fun navigateToDetail(birdJsonString: String) {
        navController.navigate(Destinations.Detail(birdJsonString = birdJsonString))
    }


}