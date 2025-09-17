package com.aal.myanmarbirds.ui.navigation

import androidx.navigation.NavController

class NavigatorImpl(private val navController: NavController) : Navigator {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun navigateToHome() {
        navController.navigate(Destinations.Home)
    }


}