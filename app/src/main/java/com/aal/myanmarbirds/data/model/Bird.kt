package com.aal.myanmarbirds.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Bird(
    val id: String = "",
    val name: String? = null,
    val englishName: String = "",
    val japaneseName: String = "",
    val order: String = "",
    val family: String = "",
    val body: String? = null,
    val head: String? = null,
    val scientificName: String = "",
    val description: String = "",
    val imageNames: List<Int>? = null,
    val audioFileName: Int? = null
)
