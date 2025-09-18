package com.aal.myanmarbirds.data.model

import java.util.UUID

data class Bird(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val englishName: String,
    val japaneseName: String,
    val body: String,
    val head: String,
    val imageNames: List<Int> // Use resource IDs (R.drawable.xxx) for images
)
