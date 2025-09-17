package com.aal.myanmarbirds.ui.feature.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsTypographyTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MBTopAppBar(text: String, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MyanmarBirdsTypographyTokens.Label,

            )
        },
        navigationIcon = {
//                IconButton(onClick = { onEvent() }) {
//                    Icon(
//                        modifier = Modifier
//                            .width(8.68.dp)
//                            .height(16.dp),
//                        imageVector = ImageVector.vectorResource(ic_chevron_left),
//                        contentDescription = "navBack"
//                    )
//                }
        },

    )
}