package com.kuzmin.playlist.domain.mediaplayer.impl

import com.kuzmin.playlist.domain.mediaplayer.iterators.MediaPlayerIteractor
import com.kuzmin.playlist.domain.mediaplayer.repository.MediaPlayerRepository

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
        mediaPlayerRepository.startMediaPlayer()
    }
    override fun pauseMediaPlayer() {
        mediaPlayerRepository.pauseMediaPlayer()
    }
}