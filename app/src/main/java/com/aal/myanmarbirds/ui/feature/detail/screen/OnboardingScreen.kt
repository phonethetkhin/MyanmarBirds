package com.aal.myanmarbirds.ui.feature.detail.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsColor
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsTypographyTokens
import com.aal.myanmarbirds.util.circleClickable

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })

    val pages = listOf(
        Pair(
            "Welcome to Birds of Myanmar",
            "Discover and learn about the beautiful birds found in Myanmar."
        ),
        Pair(
            "Search by Name or Features",
            "Easily find birds by their name, body color, head color, or wing color."
        ),
        Pair(
            "Save Your Observations",
            "Keep track of your bird sightings and revisit them anytime."
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(2f))

        HorizontalPager(state = pagerState) { page ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = pages[page].first,
                    style = MyanmarBirdsTypographyTokens.Header.copy(
                        color = MyanmarBirdsColor.current.black,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = pages[page].second,
                    style = MyanmarBirdsTypographyTokens.Label.copy(
                        color = MyanmarBirdsColor.current.black,
                        fontWeight = FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Indicators
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) MyanmarBirdsColor.current.close_blue
                            else MyanmarBirdsColor.current.black.copy(alpha = 0.3f)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = "Close", style = MyanmarBirdsTypographyTokens.Label.copy(
                color = MyanmarBirdsColor.current.close_blue,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .circleClickable { onCloseClick() }
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))


    }
}
