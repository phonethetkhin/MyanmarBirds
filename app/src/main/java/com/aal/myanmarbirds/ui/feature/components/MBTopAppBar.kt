package com.aal.myanmarbirds.ui.feature.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
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
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsTypographyTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MBTopAppBar(
    text: String,
    onLightbulbClick: () -> Unit,
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
            IconButton(onClick = onLightbulbClick) {
                Icon(
                    imageVector = Icons.Filled.Lightbulb,
                    contentDescription = "Onboarding",
                    tint = Color(0xFFFFD700), // gold
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        actions = {

        }
    )
}
