package com.aal.myanmarbirds.util

import android.content.Context
import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AudioPlayer(context: Context, audioResId: Int) {

    private val mediaPlayer: MediaPlayer = MediaPlayer.create(context, audioResId)

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var timerJob: Job? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentTime = MutableStateFlow(0L)
    val currentTime: StateFlow<Long> = _currentTime

    private val _duration = MutableStateFlow(mediaPlayer.duration.toLong())
    val duration: StateFlow<Long> = _duration

    init {
        mediaPlayer.setOnCompletionListener {
            pauseAudio()
            _currentTime.value = 0L
        }
    }

    fun playAudio() {
        mediaPlayer.start()
        _isPlaying.value = true
        startTimer()
    }

    fun pauseAudio() {
        mediaPlayer.pause()
        _isPlaying.value = false
        stopTimer()
    }

    fun seekToTime(timeMs: Long) {
        mediaPlayer.seekTo(timeMs.toInt())
        _currentTime.value = timeMs
        if (_isPlaying.value) mediaPlayer.start()
    }

    private fun startTimer() {
        stopTimer()
        timerJob = coroutineScope.launch {
            while (_isPlaying.value) {
                _currentTime.value = mediaPlayer.currentPosition.toLong()
                delay(100L)
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun release() {
        stopTimer()
        mediaPlayer.release()
    }
}
