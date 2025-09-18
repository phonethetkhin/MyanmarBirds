package com.aal.myanmarbirds.ui.feature.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HorizontalColorSelector(
    title: String,
    colors: List<String>,
    selectedColor: String,
    onColorSelected: (String) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 16.dp))
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Button(
                    onClick = { onColorSelected("") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedColor.isEmpty()) Color.Blue.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text("အားလုံး", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
            items(colors) { color ->
                Button(
                    onClick = { onColorSelected(color) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedColor == color) Color.Blue.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text(color, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
