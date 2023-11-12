package com.kuzmin.playlist.domain.repository.mediaplayer

interface MediaPlayerRepository {
    fun initPreparePlayer(previewUrl: String, listener: MediaPlayerListener)
    fun playStartControl()
    fun releseMediaPlayer()
    fun startMediaPlayer()
    fun pauseMediaPlayer()
    interface MediaPlayerListener {
        fun preparedPlayer()
        fun completionPlayer()
        fun startPlayer()
        fun pausePlayer()
        fun currentTimeMusic(timeInt: Int)
    }
}