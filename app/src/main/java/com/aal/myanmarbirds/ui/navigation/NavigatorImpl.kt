package com.aal.myanmarbirds.ui.navigation

import androidx.navigation.NavController

class NavigatorImpl(private val navController: NavController) : Navigator {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun navigateToDetail(birdJsonString: String) {
        navController.navigate(Destinations.Detail(birdJsonString = birdJsonString))
    }


}