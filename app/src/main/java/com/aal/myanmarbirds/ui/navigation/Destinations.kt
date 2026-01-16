package com.aal.myanmarbirds.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destinations {
    @Serializable
    data object Splash : Destinations

    @Serializable
    data object OnBoarding : Destinations

    @Serializable
    data object Home : Destinations

    @Serializable
    data class Detail(val birdJsonString: String) : Destinations


}