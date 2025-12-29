package com.aal.myanmarbirds.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class MyanmarBirdsColor(
    val white: Color = Color(0xFFFFFFFF),
    val black: Color = Color(0xFF000000),
    val dark_gray_50: Color = Color(0xFF202020),

    val gray_50: Color = Color(0xFFF6F6F6),
    val gray_75: Color = Color(0xFFEAEAEA),
    val gray_100: Color = Color(0xFFDDDDDD),
    val gray_200: Color = Color(0xFFC6C6C6),
    val gray_300: Color = Color(0xFFB0B0B0),
    val gray_400: Color = Color(0xFF9B9B9B),
    val gray_500: Color = Color(0xFF868686),
    val gray_600: Color = Color(0xFF727272),
    val gray_700: Color = Color(0xFF5E5E5E),
    val gray_800: Color = Color(0xFF4B4B4B),
    val gray_900: Color = Color(0xFF393939),

    val blue_50: Color = Color(0xFFE9EFF4),
    val blue_100: Color = Color(0xFFD3DFE9),
    val blue_200: Color = Color(0xFFA7C0D3),
    val blue_300: Color = Color(0xFF7BA0BE),
    val blue_400: Color = Color(0xFF4F81A8),
    val blue_500: Color = Color(0xFF236192), // Main
    val blue_600: Color = Color(0xFF1C4E75),
    val blue_700: Color = Color(0xFF153A58),
    val blue_800: Color = Color(0xFF0E273A),
    val blue_900: Color = Color(0xFF0B1D2C),


    val dark_blue_50: Color = Color(0xFFE6EBF1),
    val dark_blue_100: Color = Color(0xFFCCD8E2),
    val dark_blue_200: Color = Color(0xFF99B0C6),
    val dark_blue_300: Color = Color(0xFF6689A9),
    val dark_blue_400: Color = Color(0xFF33618D),
    val dark_blue_500: Color = Color(0xFF003A70),
    val dark_blue_600: Color = Color(0xFF002E5A),
    val dark_blue_700: Color = Color(0xFF002343),
    val dark_blue_800: Color = Color(0xFF00172D),
    val dark_blue_900: Color = Color(0xFF001122),

    val gold_50: Color = Color(0xFFF8F4F0),
    val gold_100: Color = Color(0xFFF1E9E2),
    val gold_200: Color = Color(0xFFE2D3C4),
    val gold_300: Color = Color(0xFFD4BEA7),
    val gold_400: Color = Color(0xFFC5A889),
    val gold_500: Color = Color(0xFFB7926C),
    val gold_600: Color = Color(0xFF927556),
    val gold_700: Color = Color(0xFF6E5841),
    val gold_800: Color = Color(0xFF493A2B),
    val gold_900: Color = Color(0xFF372C20),

    val teal_50: Color = Color(0xFFEBF6F5),
    val teal_100: Color = Color(0xFFD6EDEA),
    val teal_200: Color = Color(0xFFAEDCD6),
    val teal_300: Color = Color(0xFF85CAC1),
    val teal_400: Color = Color(0xFF5DB9AD),
    val teal_500: Color = Color(0xFF34A798),
    val teal_600: Color = Color(0xFF2A867A),
    val teal_700: Color = Color(0xFF1F645B),
    val teal_800: Color = Color(0xFF15433D),
    val teal_900: Color = Color(0xFF10322E),

    val error_50: Color = Color(0xFFFEF3F2),
    val error_100: Color = Color(0xFFFEE4E2),
    val error_200: Color = Color(0xFFFECDCA),
    val error_300: Color = Color(0xFFFDA29B),
    val error_400: Color = Color(0xFFF97066),
    val error_500: Color = Color(0xFFE8382B),
    val error_600: Color = Color(0xFFD92D20),
    val error_700: Color = Color(0xFFB42318),
    val error_800: Color = Color(0xFF912018),
    val error_900: Color = Color(0xFF7A271A),

    val warning_50: Color = Color(0xFFFFFAEB),
    val warning_100: Color = Color(0xFFFEF0C7),
    val warning_200: Color = Color(0xFFFEDF89),
    val warning_300: Color = Color(0xFFFEC84B),
    val warning_400: Color = Color(0xFFFDB022),
    val warning_500: Color = Color(0xFFF79009),
    val warning_600: Color = Color(0xFFEA7208),
    val warning_700: Color = Color(0xFFB54708),
    val warning_800: Color = Color(0xFF93370D),
    val warning_900: Color = Color(0xFF7A2E0E),

    val success_50: Color = Color(0xFFECFDF3),
    val success_100: Color = Color(0xFFD1FADF),
    val success_200: Color = Color(0xFFA6F4C5),
    val success_300: Color = Color(0xFF6CE9A6),
    val success_400: Color = Color(0xFF32D583),
    val success_500: Color = Color(0xFF12B76A),
    val success_600: Color = Color(0xFF039855),
    val success_700: Color = Color(0xFF027A48),
    val success_800: Color = Color(0xFF05603A),
    val success_900: Color = Color(0xFF054F31),

    val info_50: Color = Color(0xFFEFF8FF),
    val info_100: Color = Color(0xFFD1E9FF),
    val info_200: Color = Color(0xFFB2DDFF),
    val info_300: Color = Color(0xFF84CAFF),
    val info_400: Color = Color(0xFF53B1FD),
    val info_500: Color = Color(0xFF2E90FA),
    val info_600: Color = Color(0xFF1570EF),
    val info_700: Color = Color(0xFF175CD3),
    val info_800: Color = Color(0xFF1849A9),
    val info_900: Color = Color(0xFF194185),


    val backarrow_blue: Color = Color(0xFF10247A),


    ) {
    companion object {
        val current = MyanmarBirdsColor()
    }
}