package com.kuzmin.playlist.domain.repository


interface MediaPlayerRepository {
    fun initPreparePlayer(previewUrl: String, listener: MediaPlayerListener)
    fun playStartControl()
    fun releseMediaPlayer()
    fun startMediaPlayer()
    fun pauseMediaPlayer()
}