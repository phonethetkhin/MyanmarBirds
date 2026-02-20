package com.aal.myanmarbirds.ui.feature.observations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsColor
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsTypographyTokens

@Composable
fun ObservationTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    minLines: Int = 1,
    maxLines: Int = 1
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        Text(
            text = label,
            style = MyanmarBirdsTypographyTokens.Body.copy(
                color = MyanmarBirdsColor.current.gray_800,
                fontWeight = FontWeight.Bold
            )
        )

        var isFocused by remember { mutableStateOf(false) }

        val borderColor =
            if (isFocused) MyanmarBirdsColor.current.blue_500
            else MyanmarBirdsColor.current.gray_300

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            minLines = minLines,
            maxLines = maxLines,
            textStyle = MyanmarBirdsTypographyTokens.Body.copy(
                color = MyanmarBirdsColor.current.black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }
                .drawBehind {
                    val strokeWidth = 2.dp.toPx()

                    // ✅ Bottom border only
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )
                }
                .padding(vertical = 12.dp)
        )
    }
}