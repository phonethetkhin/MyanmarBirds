package com.aal.myanmarbirds.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aal.myanmarbirds.R

val openSansFamily = FontFamily(
    Font(R.font.opensans_regular, FontWeight.Normal),
    Font(R.font.opensans_medium, FontWeight.Medium),
    Font(R.font.opensans_semibold, FontWeight.SemiBold),
    Font(R.font.opensans_bold, FontWeight.Bold),
    Font(R.font.opensans_black, FontWeight.Black)
)


@Immutable
data class MyanmarBirdsTypography(
    val Header: TextStyle = MyanmarBirdsTypographyTokens.Header,
    val Label: TextStyle = MyanmarBirdsTypographyTokens.Label,
    val Body: TextStyle = MyanmarBirdsTypographyTokens.Body,

    )


internal object MyanmarBirdsTypographyTokens {

    val Header
        get() = TextStyle(
            fontSize = 32.sp,
            lineHeight = TextUnit((32 * (117 / 100)).toFloat(), TextUnitType.Sp),
            fontFamily = openSansFamily,
            color = MyanmarBirdsColor.current.blue_500,
            fontWeight = FontWeight.Normal,
            letterSpacing = 1.sp
        )

    val Label
        get() = TextStyle(
            fontSize = 14.sp,
            lineHeight = TextUnit((14 * (157 / 100)).toFloat(), TextUnitType.Sp),
            fontFamily = openSansFamily,
            color = MyanmarBirdsColor.current.gray_800,
            fontWeight = FontWeight.SemiBold
        )
    val Body
        get() = TextStyle(
            fontSize = 16.sp,
            lineHeight = 16.sp,
            fontFamily = openSansFamily,
            color = MyanmarBirdsColor.current.white,
            fontWeight = FontWeight.Normal
        )


}

@Preview
@Composable
private fun RwsLoyaltyTypographyPreview() {
    MyanmarBirdPreview {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Header",
                style = Theme.typography.Header
            )
            Text(
                text = "Label",
                style = Theme.typography.Label
            )
            Text(
                text = "Body",
                style = Theme.typography.Body
            )
        }
    }
}