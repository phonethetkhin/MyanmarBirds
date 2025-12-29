@file:OptIn(ExperimentalFoundationApi::class)

package com.aal.myanmarbirds.ui.feature.detail.screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.aal.myanmarbirds.R
import com.aal.myanmarbirds.data.model.Bird
import com.aal.myanmarbirds.ui.base.EventHandler
import com.aal.myanmarbirds.ui.feature.components.MBTopAppBar
import com.aal.myanmarbirds.ui.feature.components.PagerIndicator
import com.aal.myanmarbirds.ui.feature.detail.viewmodel.DetailScreenEvent
import com.aal.myanmarbirds.ui.feature.detail.viewmodel.DetailViewModel
import com.aal.myanmarbirds.util.AudioPlayer
import com.google.gson.Gson


@Composable
fun DetailScreen(
    birdJsonString: String,
    detailViewModel: DetailViewModel = hiltViewModel(),
    onEvent: (DetailScreenEvent) -> Unit
) {
    val state by detailViewModel.uiState.collectAsState()

    EventHandler(detailViewModel) { event ->
        onEvent(event)
    }

    val bird: Bird = remember {
        Gson().fromJson(birdJsonString, Bird::class.java)
    }

    Scaffold(
        containerColor = Color.Gray.copy(0.2f),
        topBar = {
            MBTopAppBar(
                text = "ဘဲကျားလေး",
                onLightbulbClick = {},
                onMemoClick = {}
            )
        }
    ) { innerPadding ->
        DetailScreenContent(
            bird = bird,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@Composable
fun DetailScreenContent(
    bird: Bird,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val audioPlayer = remember {
        AudioPlayer(context, bird.audioResId)
    }

    DisposableEffect(Unit) {
        onDispose { audioPlayer.release() }
    }

    Column(modifier = modifier) {

        /* ---------------- IMAGE PAGER ---------------- */

        val pagerState = rememberPagerState(
            pageCount = { bird.imageNames.size }
        )

        var currentScale by remember { mutableStateOf(1f) }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            beyondViewportPageCount = 1,
            overscrollEffect = null
        ) { page ->

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                AsyncImage(
                    model = bird.imageNames[page],
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }



        Spacer(modifier = Modifier.height(8.dp))

        PagerIndicator(
            pageCount = bird.imageNames.size,
            currentPage = pagerState.currentPage
        )

        /* ---------------- SCROLLABLE CONTENT ---------------- */

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            AudioPlayerUI(audioPlayer)

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()

            Column(modifier = Modifier.padding(16.dp)) {
                InfoRow("မျိုးစဥ်(order):", bird.order)
                InfoRow("မျိုးရင်း(family):", bird.family)
                InfoRow(
                    "သိပ္ပံအမည်:",
                    bird.scientificName,
                    italic = true
                )
                InfoRow("အင်္ဂလိပ်အမည်:", bird.englishName)
                InfoRow("ဂျပန်အမည်:", bird.japaneseName)
            }

            HorizontalDivider()

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "အကြောင်းအရာများ",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(bird.description)
            }
        }
    }
}

/* ---------------- INFO ROW ---------------- */

@Composable
fun InfoRow(
    label: String,
    value: String,
    italic: Boolean = false
) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(
            label,
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            value,
            fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal
        )
    }
}

/* ---------------- AUDIO UI ---------------- */

@Composable
fun AudioPlayerUI(audioPlayer: AudioPlayer) {
    val isPlaying by audioPlayer.isPlaying.collectAsState()
    val currentTime by audioPlayer.currentTime.collectAsState()
    val duration by audioPlayer.duration.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = {
                    if (isPlaying) {
                        audioPlayer.pauseAudio()
                    } else {
                        audioPlayer.playAudio()
                    }
                }
            ) {
                Icon(
                    painter = painterResource(
                        if (isPlaying) {
                            R.drawable.outline_pause_circle_24
                        } else {
                            R.drawable.outline_play_circle_24
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            Text(formatTime(currentTime), color = Color.Gray)
            Spacer(modifier = Modifier.weight(1f))
            Text(formatTime(duration), color = Color.Gray)
        }

        Slider(
            value = currentTime.toFloat(),
            onValueChange = {
                audioPlayer.seekToTime(it.toLong())
            },
            valueRange = 0f..duration.toFloat()
        )
    }
}

/* ---------------- TIME FORMAT ---------------- */

fun formatTime(timeMs: Long): String {
    val totalSeconds = timeMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
