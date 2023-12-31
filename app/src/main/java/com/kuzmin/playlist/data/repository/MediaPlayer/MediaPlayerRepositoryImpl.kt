package com.kuzmin.playlist.data.repository.MediaPlayer

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.kuzmin.playlist.domain.mediaplayer.repository.MediaPlayerRepository

class MediaPlayerRepositoryImpl(
    private var mediaPlayer: MediaPlayer
): MediaPlayerRepository {
    private var playerState = STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var runnable: Runnable
    private lateinit var listener: MediaPlayerRepository.MediaPlayerListener

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
            handler.removeCallbacks(runnable)
            playerState = STATE_PREPARED
            listener.completionPlayer()
        }
        runnable = object : Runnable {
            override fun run() {
                handler.postDelayed(this, TIME_DEBOUNCE_DELAY_MILLIS)
                listener.currentTimeMusic(mediaPlayer.currentPosition)
            }
        }
        this.listener = listener
    }

    override fun playStartControl() {
        when (playerState) {
            STATE_PLAYING -> {
                handler.removeCallbacks(runnable)
                mediaPlayer.pause()
                playerState = STATE_PAUSED
                listener.pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                listener.startPlayer()
                handler.post(runnable)
                mediaPlayer.start()
                playerState = STATE_PLAYING
            }
        }
    }

    override fun releseMediaPlayer() {
        mediaPlayer.release()
        handler.removeCallbacks(runnable)
    }

    override fun startMediaPlayer() {
        if (!handler.hasCallbacks(runnable)) {
            handler.post(runnable)
        }
        mediaPlayer.start()
        playerState = STATE_PLAYING
        listener.startPlayer()
    }

    override fun pauseMediaPlayer() {
        if (!handler.hasCallbacks(runnable)) {
            handler.removeCallbacks(runnable)
        }
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        listener.pausePlayer()
    }
}
