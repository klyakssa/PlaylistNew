package com.kuzmin.playlist.domain.repository

interface MediaPlayerListener {
    fun preparedPlayer()
    fun completionPlayer()
    fun startPlayer()
    fun pausePlayer()
    fun currentTimeMusic(timeInt: Int)
}