package com.kuzmin.playlist.domain.impl.mediaplayer

import com.kuzmin.playlist.domain.iterators.mediaplayer.MediaPlayerIteractor
import com.kuzmin.playlist.domain.repository.mediaplayer.MediaPlayerRepository

class MediaPlayerInteractionImpl (
    private val mediaPlayerRepository: MediaPlayerRepository,
): MediaPlayerIteractor {
    override fun initPreparePlayer(
        previewUrl: String,
        listener: MediaPlayerRepository.MediaPlayerListener
    ) {
        mediaPlayerRepository.initPreparePlayer(previewUrl,listener)
    }
    override fun playStartControl() {
        mediaPlayerRepository.playStartControl()
    }
    override fun releseMediaPlayer() {
        mediaPlayerRepository.releseMediaPlayer()
    }
    override fun startMediaPlayer() {
        mediaPlayerRepository.pauseMediaPlayer()
    }
    override fun pauseMediaPlayer() {
        mediaPlayerRepository.startMediaPlayer()
    }
}