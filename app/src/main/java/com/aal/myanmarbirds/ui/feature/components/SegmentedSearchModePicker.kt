package com.aal.myanmarbirds.ui.feature.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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
            .height(50.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Gray.copy(alpha = 0.6f)) // 👈 this is the row bg
            .padding(top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    )
    {
        modes.forEachIndexed { index, mode ->
            Button(
                onClick = { onModeSelected(mode) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (mode == selectedMode) Color.White
                    else Color.Gray.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(if (mode == selectedMode) 8.dp else 0.dp),
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
                VerticalDivider(height = 40.dp)
            }
        }
    }
}

@Composable
fun VerticalDivider(
    color: Color = Color.Black.copy(alpha = 0.2f),
    thickness: Dp = 1.dp,
    height: Dp = 30.dp, // pick a height close to your Button height
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .height(30.dp)
            .width(thickness)
            .background(color)
    )
}




