package com.aal.myanmarbirds.ui.feature.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aal.myanmarbirds.R
import com.aal.myanmarbirds.data.model.Bird

@Composable
fun BirdListItem(bird: Bird, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageModifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
        if (bird.imageNames.isNotEmpty()) {
            Image(
                painter = painterResource(id = bird.imageNames.first()),
                contentDescription = bird.name,
                modifier = imageModifier
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = "Placeholder",
                modifier = imageModifier
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = bird.name, fontWeight = FontWeight.Bold)
            Text(text = bird.englishName, color = Color.Gray)
        }
    }
}
