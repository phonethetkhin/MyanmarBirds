package com.aal.myanmarbirds.ui.feature.observations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aal.myanmarbirds.data.model.SearchResult
import com.aal.myanmarbirds.util.clickable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

@Composable
fun LocationSearchBar(
    onLocationSelected: (Double, Double, String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<SearchResult>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    Box {

        Column {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it

                    if (it.length <= 2) {
                        searchResults = emptyList()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = { Text("Search location...") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        }

        LaunchedEffect(searchQuery) {
            kotlinx.coroutines.delay(500)

            if (searchQuery.length > 2) {
                isLoading = true

                searchLocation(searchQuery) { results ->
                    searchResults = results
                    isLoading = false
                }
            }
        }

        if (searchResults.isNotEmpty() || isLoading) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 70.dp)
                    .heightIn(max = 250.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {

                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        searchResults.take(5).forEachIndexed { index, result ->

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        searchQuery = result.name
                                        searchResults = emptyList()

                                        onLocationSelected(
                                            result.lat,
                                            result.lng,
                                            result.name
                                        )
                                    }
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                Text(
                                    text = result.name,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }

                            if (index != searchResults.take(5).lastIndex) {
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}
fun searchLocation(
    query: String,
    onResult: (List<SearchResult>) -> Unit
) {
    val apiKey = "UE0iWVkk5q3sPd9VyYbv"

    val url =
        "https://api.maptiler.com/geocoding/$query.json?key=$apiKey"

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = URL(url).readText()
            val json = JSONObject(response)
            val features = json.getJSONArray("features")

            val results = mutableListOf<SearchResult>()

            for (i in 0 until features.length()) {
                val feature = features.getJSONObject(i)
                val placeName = feature.getString("place_name")

                val center = feature.getJSONArray("center")
                val lng = center.getDouble(0)
                val lat = center.getDouble(1)

                results.add(
                    SearchResult(
                        name = placeName,
                        lat = lat,
                        lng = lng
                    )
                )
            }

            withContext(Dispatchers.Main) {
                onResult(results)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}