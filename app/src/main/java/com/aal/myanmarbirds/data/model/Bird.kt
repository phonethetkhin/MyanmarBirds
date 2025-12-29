package com.aal.myanmarbirds.data.model

import com.aal.myanmarbirds.R
import kotlinx.serialization.Serializable

@Serializable
data class Bird(
    val id: String = "",
    val name: String = "",
    val englishName: String = "",
    val japaneseName: String = "",
    val order: String = "",
    val family: String = "",
    val body: String = "",
    val head: String = "",
    val scientificName: String = "",
    val description: String = "",
    val imageNames: List<Int> = emptyList(),
    val audioResId: Int = R.raw.anser_indicus
)
