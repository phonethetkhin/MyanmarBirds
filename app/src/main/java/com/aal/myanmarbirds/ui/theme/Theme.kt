package com.aal.myanmarbirds.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

object Theme {
    val typography: MyanmarBirdsTypography
        @Composable
        @ReadOnlyComposable get() = MyanmarBirdsTypography()

    val colors: MyanmarBirdsColor
        @Composable
        @ReadOnlyComposable get() = MyanmarBirdsColor()
}


@Composable
fun MyanmarBirdsTheme(
    isAppearanceLightStatusBars: Boolean = false,
    isAppearanceLightNavigationBars: Boolean = false,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !isAppearanceLightStatusBars
            insetsController.isAppearanceLightNavigationBars = !isAppearanceLightNavigationBars
        }
    }
    val lightColorScheme = lightColorScheme(
        primary = Theme.colors.blue_500,
        onPrimary = Theme.colors.white,
        onPrimaryContainer = Theme.colors.black,
        error = Theme.colors.error_500,
        background = Theme.colors.white,
        onBackground = Theme.colors.black,
        surface = Theme.colors.white,
        onSurface = Theme.colors.gray_800,
        outline = Theme.colors.blue_500,
        surfaceVariant = Theme.colors.black,
        outlineVariant = Theme.colors.black,
        surfaceContainer = Theme.colors.white,
        surfaceContainerLow = Theme.colors.white,
        primaryContainer = Theme.colors.white,
        surfaceContainerHighest = Theme.colors.white,
    )

    MaterialTheme(
        colorScheme = lightColorScheme
    ) {
        CompositionLocalProvider(
            content = content
        )
    }
}

@Composable
fun MyanmarBirdPreview(
    content: @Composable BoxWithConstraintsScope.() -> Unit,
) {
    MyanmarBirdsTheme {
        Surface {
            BoxWithConstraints {
                content()
            }
        }
    }
}
