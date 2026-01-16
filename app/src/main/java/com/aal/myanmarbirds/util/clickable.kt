package com.aal.myanmarbirds.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

fun Modifier.clickable(
    enabled: Boolean = true,
    enableRipple: Boolean = false,
    onClick: () -> Unit
): Modifier {
    return composed {
        val interactionSource = remember {
            if (enableRipple) {
                MutableInteractionSource()
            } else {
                NoRippleInteractionSource()
            }
        }
        this.clickable(
            enabled = enabled,
            interactionSource = interactionSource,
            indication = if (enableRipple) ripple() else null
        ) {
            onClick()
        }
    }
}

fun Modifier.circleClickable(
    enabled: Boolean = true,
    enableRipple: Boolean = true,
    rippleColor: Color = Color.Unspecified,
    bounded: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }

    val rippleIndication = if (enableRipple) {
        ripple(
            bounded = bounded,
            color = rippleColor
        )
    } else {
        null
    }

    this
        .clip(CircleShape)
        .clickable(
            enabled = enabled,
            interactionSource = interactionSource,
            indication = rippleIndication,
            onClick = onClick
        )
}

