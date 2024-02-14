package com.kuzmin.playlist.data.repository.MediaPlayer

import android.media.MediaPlayer
import com.kuzmin.playlist.data.repository.MediaPlayer.MediaPlayerRepositoryImpl.MediaPlayerState.*
import com.kuzmin.playlist.domain.mediaplayer.repository.MediaPlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaPlayerRepositoryImpl(
    private var mediaPlayer: MediaPlayer
): MediaPlayerRepository {
    private var playerState = DEFAULT

    private lateinit var listener: MediaPlayerRepository.MediaPlayerListener
    private val myCoroutineScope = CoroutineScope(Dispatchers.Default)
    companion object {
        private const val TIME_DEBOUNCE_DELAY_MILLIS = 300L
    }

    enum class MediaPlayerState {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED
    }

    override fun initPreparePlayer(previewUrl: String, listener: MediaPlayerRepository.MediaPlayerListener) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PREPARED
            listener.preparedPlayer()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PREPARED
            listener.completionPlayer()
        }

        this.listener = listener
    }

    private fun startTimer() {
        myCoroutineScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(TIME_DEBOUNCE_DELAY_MILLIS)
                withContext(Dispatchers.Main) {
                    listener.currentTimeMusic(mediaPlayer.currentPosition)
                }
            }
            withContext(Dispatchers.Main) {
                listener.currentTimeMusic(0)
            }
        }
    }

    private fun cancelTimer() {
        myCoroutineScope.cancel() // отменяем все корутины в этой области
    }

    override fun playStartControl() {
        when (playerState) {
            PLAYING -> {
                pauseMediaPlayer()
            }
            PREPARED, PAUSED -> {
                startMediaPlayer()
            }
            else -> {}
        }
    }

    override fun releseMediaPlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        playerState = DEFAULT
    }

    override fun startMediaPlayer() {
        mediaPlayer.start()
        startTimer()
        playerState = PLAYING
        listener.startPlayer()
    }

    override fun pauseMediaPlayer() {
        cancelTimer()
        mediaPlayer.pause()
        playerState = PAUSED
        listener.pausePlayer()
    }
}
