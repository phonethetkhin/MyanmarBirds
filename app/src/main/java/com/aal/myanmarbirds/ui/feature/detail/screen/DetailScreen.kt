@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.aal.myanmarbirds.ui.feature.detail.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsColor
import com.aal.myanmarbirds.ui.theme.MyanmarBirdsTypographyTokens
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
        topBar = {
            MBTopAppBar(
                text = "ဘဲကျားလေး",
                onLightbulbClick = {},
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

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {

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
                    .padding(16.dp),
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

        PagerIndicator(
            pageCount = bird.imageNames.size,
            currentPage = pagerState.currentPage
        )

        /* ---------------- SCROLLABLE CONTENT ---------------- */

        Column(
            modifier = Modifier

                .padding(bottom = 16.dp)
        ) {

            AudioPlayerUI(audioPlayer)
            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(color = MyanmarBirdsColor.current.divider_gray)

            Column(modifier = Modifier.padding(16.dp)) {
                InfoRow(
                    label = "မျိုးစဥ်(order):",
                    value = bird.order,
                    textStyle = MyanmarBirdsTypographyTokens.Body.copy(
                        fontWeight = FontWeight.Bold,
                        color = MyanmarBirdsColor.current.text_yellow
                    )
                )
                InfoRow(
                    label = "မျိုးရင်း(family):",
                    value = bird.family,
                    textStyle = MyanmarBirdsTypographyTokens.Body.copy(
                        fontWeight = FontWeight.Bold,
                        color = MyanmarBirdsColor.current.text_yellow
                    )
                )
                InfoRow(
                    label = "သိပ္ပံအမည်:",
                    value = bird.scientificName,
                    textStyle = MyanmarBirdsTypographyTokens.Body.copy(
                        fontWeight = FontWeight.Bold,
                        color = MyanmarBirdsColor.current.black
                    )
                )
                InfoRow(
                    label = "အင်္ဂလိပ်အမည်:",
                    value = bird.englishName,
                    textStyle = MyanmarBirdsTypographyTokens.Body.copy(
                        fontWeight = FontWeight.Bold,
                        color = MyanmarBirdsColor.current.black
                    )
                )
                InfoRow(
                    label = "ဂျပန်အမည်:",
                    value = bird.japaneseName,
                    textStyle = MyanmarBirdsTypographyTokens.Body.copy(
                        color = MyanmarBirdsColor.current.gray_800
                    )
                )
            }

            HorizontalDivider(color = MyanmarBirdsColor.current.divider_gray)

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "အကြောင်းအရာများ",
                    style = MyanmarBirdsTypographyTokens.Body.copy(
                        color = MyanmarBirdsColor.current.gray_800,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = bird.description,
                    style = MyanmarBirdsTypographyTokens.Body.copy(
                        fontWeight = FontWeight.Bold,
                        color = MyanmarBirdsColor.current.black
                    )
                )
            }
        }
    }
}

/* ---------------- INFO ROW ---------------- */

@Composable
fun InfoRow(
    label: String,
    value: String,
    textStyle: TextStyle
) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(
            text = label,
            style = MyanmarBirdsTypographyTokens.Body.copy(
                color = MyanmarBirdsColor.current.gray_800,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            style = textStyle
        )
    }
}

/* ---------------- AUDIO UI ---------------- */
@Composable
fun AudioPlayerUI(audioPlayer: AudioPlayer) {
    val isPlaying by audioPlayer.isPlaying.collectAsState()
    val currentTime by audioPlayer.currentTime.collectAsState()
    val duration by audioPlayer.duration.collectAsState()
    val buffering by audioPlayer.buffering.collectAsState()

    // Use rememberSaveable to survive configuration changes
    var sliderPosition by rememberSaveable { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    val sliderValue by remember(isDragging, sliderPosition, currentTime, duration) {
        derivedStateOf {
            when {
                isDragging -> sliderPosition
                duration > 0 -> currentTime.toFloat()
                else -> 0f
            }
        }
    }


    Column(modifier = Modifier.padding(horizontal = 16.dp)) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = {
                    if (isPlaying) audioPlayer.pauseAudio()
                    else audioPlayer.playAudio()
                },
                enabled = !buffering
            ) {
                Icon(
                    painter = painterResource(
                        if (isPlaying) R.drawable.pause_circle_svgrepo_com
                        else R.drawable.play_circle_svgrepo_com
                    ),
                    tint = if (buffering) Color.Gray else MyanmarBirdsColor.current.play_green,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                formatTime(sliderValue.toLong()),
                style = MyanmarBirdsTypographyTokens.Body.copy(
                    color = MyanmarBirdsColor.current.black
                )
            )


            Spacer(modifier = Modifier.weight(1f))

            Text(
                formatTime(duration),
                style = MyanmarBirdsTypographyTokens.Body.copy(
                    color = MyanmarBirdsColor.current.black
                )
            )
        }

        Slider(
            value = sliderValue,
            valueRange = 0f..maxOf(1f, duration.toFloat()),
            onValueChange = {
                isDragging = true
                sliderPosition = it
            },
            onValueChangeFinished = {
                isDragging = false
                audioPlayer.seekToTime(sliderPosition.toLong())
            },
            enabled = !buffering,

            thumb = {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = MyanmarBirdsColor.current.play_green,
                            shape = CircleShape
                        )
                )
            },

            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    colors = SliderDefaults.colors(
                        activeTrackColor = MyanmarBirdsColor.current.play_green,
                        inactiveTrackColor = Color.LightGray
                    ),
                    modifier = Modifier.height(3.dp)
                )
            }
        )


    }
}

fun formatTime(timeMs: Long): String {
    val totalSeconds = timeMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
