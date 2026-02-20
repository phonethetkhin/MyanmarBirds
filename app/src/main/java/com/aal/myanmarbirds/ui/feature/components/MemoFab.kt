package com.aal.myanmarbirds.ui.feature.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MemoButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, bottom = 16.dp), // similar to SwiftUI's bottom bar spacing
        horizontalArrangement = Arrangement.Start
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50) // replace with your CustomGreen
            ),
            shape = RoundedCornerShape(12.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "မှတ်တမ်း",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Filled.Book,
                    contentDescription = "Memo",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}


enum class SearchMode(val display: String) {
    NAME("အမည်🔤"),
    BODY("ခန္ဓာကိုယ်အရောင်🐦‍"),
    HEAD("ဦးခေါင်းအရောင်🐦")
}

enum class BodyColors(val display: String) {
    BROWN("🤎"), WHITE("🤍"), GRAY("🩶"), BLACK("🖤"),
    PURPLE("💜"), BLUE("💙"), GREEN("💚"), YELLOW("💛"),
    ORANGE("🧡"), RED("❤️"), PINK("🩷")
}

enum class HeadColors(val display: String) {
    BROWN("🐦🤎"), WHITE("🐦🤍"), GRAY("🐦🩶"), BLACK("🐦🖤"),
    PURPLE("🐦💜"), BLUE("🐦💙"), GREEN("🐦💚"), YELLOW("🐦💛"),
    ORANGE("🐦🧡"), RED("🐦❤️"), PINK("🐦🩷")
}


