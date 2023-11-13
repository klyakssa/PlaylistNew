package com.kuzmin.playlist.domain.mediaplayer.iterators

import com.kuzmin.playlist.domain.mediaplayer.repository.MediaPlayerRepository


interface MediaPlayerIteractor {
    fun initPreparePlayer(previewUrl: String, listener: MediaPlayerRepository.MediaPlayerListener)
    fun playStartControl()
    fun releseMediaPlayer()
    fun startMediaPlayer()
    fun pauseMediaPlayer()
}