package com.aal.myanmarbirds.ui.feature.observations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsColor
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsTypographyTokens

@Composable
fun AddObservationBottomSheet(onDoneClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(0.2f)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Cancel",
                style = MyanmarBirdsTypographyTokens.Body.copy(
                    color = MyanmarBirdsColor.current.close_blue
                )
            )
            Text(
                text = "Save",
                style = MyanmarBirdsTypographyTokens.Body.copy(
                    color = MyanmarBirdsColor.current.close_blue
                )
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Add Observation",
            style = MyanmarBirdsTypographyTokens.Header.copy(
                color = MyanmarBirdsColor.current.black,
                fontWeight = FontWeight.Black
            )
        )

        Column(
            modifier = Modifier.background(
                MyanmarBirdsColor.current.white,
                shape = RoundedCornerShape(8.dp)
            )
        ) {
            Row {
                Text(
                    "Date",
                    style = MyanmarBirdsTypographyTokens.Body
                )
                Surface(color = Color.Gray.copy(alpha = 0.2f)) {
                    Text("Aug 29, 2025", style = MyanmarBirdsTypographyTokens.Body)
                }
            }

            HorizontalDivider()
        }

    }
}

