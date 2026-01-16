package com.aal.myanmarbirds.ui.feature.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsColor
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsTypographyTokens
import com.aal.myanmarbirds.util.clickable

@Composable
fun SegmentedColorSelector(
    title: String,
    colors: List<String>,
    selectedColor: String,
    onColorSelected: (String) -> Unit
) {
    val totalItems = colors.size + 1 // +1 for "All"
    val itemFraction = 1f / totalItems

    Column {

        Text(
            text = title,
            style = MyanmarBirdsTypographyTokens.Header.copy(
                fontWeight = FontWeight.Bold,
                color = MyanmarBirdsColor.current.black
            ),
            modifier = Modifier.padding(16.dp)
        )

        Surface(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            color = Color.Gray.copy(alpha = 0.2f)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {


                // ALL
                item {
                    SegmentedItem(
                        text = "All",
                        isSelected = selectedColor.isEmpty(),
                        modifier = Modifier.fillParentMaxWidth(itemFraction),
                        onClick = { onColorSelected("") }
                    )
                }

                item { VerticalDivider() }

                // COLORS
                itemsIndexed(colors) { index, color ->
                    SegmentedItem(
                        text = color,
                        isSelected = selectedColor == color,
                        modifier = Modifier.fillParentMaxWidth(itemFraction),
                        onClick = { onColorSelected(color) }
                    )

                    if (index != colors.lastIndex) {
                        VerticalDivider()
                    }
                }
            }
        }
    }
}


@Composable
private fun SegmentedItem(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable { onClick() }
            .background(
                if (isSelected) Color.Blue.copy(alpha = 0.15f)
                else Color.Transparent
            )
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}


@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .width(1.dp)
                .background(Color.LightGray)
        )
    }
}
