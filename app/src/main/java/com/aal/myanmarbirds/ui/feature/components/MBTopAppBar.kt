package com.aal.myanmarbirds.ui.feature.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsColor
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsTypographyTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MBTopAppBar(
    text: String,
    onLightbulbClick: () -> Unit,
    isHomeScreen: Boolean = false,
    onMemoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = text,
                style = MyanmarBirdsTypographyTokens.Title.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
            )
        },
        navigationIcon = {
            if (isHomeScreen) {
                IconButton(onClick = onLightbulbClick) {
                    Icon(
                        imageVector = Icons.Filled.Lightbulb,
                        contentDescription = "Onboarding",
                        tint = Color(0xFFFFD700), // gold
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                Row {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = "backarrow",
                        tint = MyanmarBirdsColor.current.backarrow_blue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "Back",
                        style = MyanmarBirdsTypographyTokens.Body.copy(
                            color = MyanmarBirdsColor.current.backarrow_blue,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

            }
        },
        actions = {

        }
    )
}
