package com.aal.myanmarbirds

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyanmarBirdsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        org.maplibre.android.MapLibre.getInstance(this)
    }
}
