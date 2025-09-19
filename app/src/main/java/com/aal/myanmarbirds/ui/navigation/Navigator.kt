package com.aal.myanmarbirds.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController

interface Navigator {
    fun navigateUp()
    fun navigateToDetail(birdJsonString: String)

}

@Composable
fun rememberNavigator(navController: NavController) = remember {
    NavigatorImpl(navController)
}