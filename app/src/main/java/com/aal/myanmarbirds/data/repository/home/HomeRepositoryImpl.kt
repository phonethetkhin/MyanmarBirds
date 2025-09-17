package com.aal.myanmarbirds.data.repository.home

import jakarta.inject.Inject

class HomeRepositoryImpl @Inject constructor(
) : HomeRepository {

    override suspend fun getItems(): List<String> {
        // This could come from API or database
        return listOf("Item 1", "Item 2", "Item 3")
    }

    override suspend fun getItemDetail(id: String): String {
        // Example fetch by id
        return "Detail for $id"
    }
}