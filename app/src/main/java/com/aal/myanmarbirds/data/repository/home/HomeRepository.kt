package com.aal.myanmarbirds.data.repository.home

// data/repository/MyRepository.kt
interface HomeRepository {
    suspend fun getItems(): List<String>
    suspend fun getItemDetail(id: String): String
}