package com.kuzmin.playlist.domain.use_case

import com.kuzmin.playlist.domain.repository.MediaPlayerListener
import com.kuzmin.playlist.domain.repository.MediaPlayerRepository

class WorkWithMediaPlayerUseCase (
    private val mediaPlayerRepository: MediaPlayerRepository,
) {
    fun playerPrepare(previewUrl: String, listener: MediaPlayerListener) {
        mediaPlayerRepository.initPreparePlayer(previewUrl,listener)
    }
    fun playStartControl() {
        mediaPlayerRepository.playStartControl()
    }
    fun releasePlayer(){
        mediaPlayerRepository.releseMediaPlayer()
    }
    fun hardPauseToPlayer(){
        mediaPlayerRepository.pauseMediaPlayer()
    }
    fun hardStartToPlayer(){
        mediaPlayerRepository.startMediaPlayer()
    }
}