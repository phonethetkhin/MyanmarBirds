package com.aal.myanmarbirds.util

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs

class AudioPlayer(
    context: Context,
    audioResId: Int
) {
    // Use Media3 ExoPlayer
    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context)
        .setSeekForwardIncrementMs(10000)
        .setSeekBackIncrementMs(10000)
        .setHandleAudioBecomingNoisy(true) // Pauses when headphones unplugged
        .build()

    private var mediaSession: MediaSession? = null

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var progressJob: Job? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentTime = MutableStateFlow(0L)
    val currentTime: StateFlow<Long> = _currentTime

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration

    private val _buffering = MutableStateFlow(false)
    val buffering: StateFlow<Boolean> = _buffering

    init {
        setupPlayer(context, audioResId)
        setupMediaSession(context)
    }

    private fun setupPlayer(context: Context, audioResId: Int) {
        val mediaItem = MediaItem.fromUri(
            "android.resource://${context.packageName}/$audioResId"
        )

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()

        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {

                    Player.STATE_READY -> {
                        _duration.value = exoPlayer.duration
                        _buffering.value = false
                    }

                    Player.STATE_BUFFERING -> {
                        _buffering.value = true
                    }

                    Player.STATE_ENDED -> {
                        // 1️⃣ Stop UI state
                        _isPlaying.value = false
                        stopProgressUpdates()

                        // 3️⃣ Sync UI
                        _currentTime.value = 0L
                    }
                }
            }


            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
                if (isPlaying) {
                    startProgressUpdates()
                } else {
                    stopProgressUpdates()
                }
            }

            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                // Handle audio focus changes
            }

            override fun onPlayerError(error: PlaybackException) {
                // Handle playback errors gracefully
                println("Playback error: ${error.errorCodeName}")
                _isPlaying.value = false
            }
        })
    }

    private fun setupMediaSession(context: Context) {
        mediaSession = MediaSession.Builder(context, exoPlayer).build()
        // Enables background playback and media controls
    }

    fun playAudio() {
        if (exoPlayer.playbackState == Player.STATE_ENDED) {
            exoPlayer.seekTo(0)
        }
        if (!exoPlayer.isPlaying) {
            exoPlayer.play()
        }
    }

    fun pauseAudio() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        }
    }

    fun seekToTime(timeMs: Long) {
        val safeTime = timeMs.coerceIn(0, _duration.value)
        exoPlayer.seekTo(safeTime)
        _currentTime.value = safeTime
    }

    fun setPlaybackSpeed(speed: Float) {
        exoPlayer.playbackParameters = exoPlayer.playbackParameters
            .withSpeed(speed.coerceIn(0.5f, 2.0f))
    }

    private fun startProgressUpdates() {
        stopProgressUpdates()

        progressJob = scope.launch {
            var lastUpdateTime = 0L

            while (isActive && exoPlayer.isPlaying) {
                val currentPos = exoPlayer.currentPosition

                // Update only if position changed significantly (reduces recompositions)
                if (abs(currentPos - lastUpdateTime) > 50) {
                    _currentTime.value = currentPos
                    lastUpdateTime = currentPos
                }

                delay(100L) // Update every 100ms - smooth enough
            }

            // Final update when stopped
            if (!exoPlayer.isPlaying) {
                _currentTime.value = exoPlayer.currentPosition
            }
        }
    }

    private fun stopProgressUpdates() {
        progressJob?.cancel()
        progressJob = null
    }

    fun release() {
        stopProgressUpdates()
        scope.cancel()
        mediaSession?.release()
        exoPlayer.release()
    }
}