package com.kuzmin.playlist.data.repository.MediaPlayer

import android.media.MediaPlayer
import com.kuzmin.playlist.domain.mediaplayer.repository.MediaPlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MediaPlayerRepositoryImpl(
    private var mediaPlayer: MediaPlayer
): MediaPlayerRepository {
    private var playerState = STATE_DEFAULT

    private lateinit var listener: MediaPlayerRepository.MediaPlayerListener
    private val myCoroutineScope = CoroutineScope(Dispatchers.Default)
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIME_DEBOUNCE_DELAY_MILLIS = 1000L
    }
    override fun initPreparePlayer(previewUrl: String, listener: MediaPlayerRepository.MediaPlayerListener) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            listener.preparedPlayer()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            listener.completionPlayer()
        }

        this.listener = listener
    }

    private fun startTimer() {
        myCoroutineScope.launch {
            // ваш код корутины
            while (mediaPlayer.isPlaying) {
                delay(TIME_DEBOUNCE_DELAY_MILLIS)
                withContext(Dispatchers.Main) {
                    listener.currentTimeMusic(mediaPlayer.currentPosition)
                }
            }
        }
    }

    private fun cancelTimer() {
        myCoroutineScope.cancel() // отменяем все корутины в этой области
    }

    override fun playStartControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pauseMediaPlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startMediaPlayer()
            }
        }
    }

    override fun releseMediaPlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        playerState = STATE_DEFAULT
    }

    override fun startMediaPlayer() {
        mediaPlayer.start()
        startTimer()
        playerState = STATE_PLAYING
        listener.startPlayer()
    }

    override fun pauseMediaPlayer() {
        cancelTimer()
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        listener.pausePlayer()
    }
}
