package com.kuzmin.playlist.domain.iterators.mediaplayer

import com.kuzmin.playlist.domain.repository.mediaplayer.MediaPlayerRepository


interface MediaPlayerIteractor {
    fun initPreparePlayer(previewUrl: String, listener: MediaPlayerRepository.MediaPlayerListener)
    fun playStartControl()
    fun releseMediaPlayer()
    fun startMediaPlayer()
    fun pauseMediaPlayer()
}