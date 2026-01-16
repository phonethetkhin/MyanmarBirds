package com.aal.myanmarbirds.ui.feature.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.aal.myanmarbirds.R
import com.aal.myanmarbirds.data.model.Bird

@Composable
fun BirdListItem(bird: Bird, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Bird image
        if (bird.imageNames.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(bird.imageNames.first()),
                contentDescription = bird.name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = rememberAsyncImagePainter(R.drawable.placeholder),
                contentDescription = bird.name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Wrap text + chevron inside a Column so divider aligns under them
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = bird.name, fontWeight = FontWeight.Bold)
                    Text(text = bird.englishName, color = Color.Gray)
                }

                Icon(
                    painter = painterResource(R.drawable.outline_chevron_right_24),
                    tint = Color.Gray.copy(0.5f),
                    contentDescription = "ChevronRightIcon"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Divider only under text+chevron
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.5f))
        }
    }
}

