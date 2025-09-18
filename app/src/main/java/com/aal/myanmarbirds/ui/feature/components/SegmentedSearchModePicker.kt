package com.aal.myanmarbirds.ui.feature.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsColor

@Composable
fun SegmentedSearchModePicker(
    selectedMode: SearchMode,
    onModeSelected: (SearchMode) -> Unit
) {
    val modes = SearchMode.entries.toTypedArray()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray.copy(alpha = 0.2f)),
    ) {
        modes.forEachIndexed { index, mode ->
            Button(
                onClick = { onModeSelected(mode) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (mode == selectedMode) Color.White else Color.Gray.copy(
                        alpha = 0.2f
                    )
                ),
                shape = RoundedCornerShape(0.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = mode.display,
                    fontSize = 14.sp,
                    color = MyanmarBirdsColor.current.black,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Add vertical divider **except after the last button**
            if (index < modes.lastIndex) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(4.dp) // match button height (adjust as needed)
                        .background(Color.Gray)
                )
            }
        }
    }
}



