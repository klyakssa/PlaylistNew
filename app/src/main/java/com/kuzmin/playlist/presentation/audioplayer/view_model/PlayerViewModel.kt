package com.kuzmin.playlist.presentation.audioplayer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuzmin.playlist.domain.mediaplayer.iterators.MediaPlayerIteractor
import com.kuzmin.playlist.domain.mediaplayer.repository.MediaPlayerRepository
import com.kuzmin.playlist.presentation.audioplayer.model.PlayerState

class PlayerViewModel(
    private val url: String,
    private val workWithMediaPlayer: MediaPlayerIteractor,
): ViewModel() {

    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = stateLiveData

    init {
        initPlayer()
    }
    private fun initPlayer(){
        workWithMediaPlayer.initPreparePlayer(
            previewUrl = url,
            listener = object : MediaPlayerRepository.MediaPlayerListener {
                override fun preparedPlayer() {
                    renderState(
                        PlayerState.Prepared
                    )
                }
                override fun completionPlayer() {
                    renderState(
                        PlayerState.Completion
                    )
                }
                override fun startPlayer() {
                    renderState(
                        PlayerState.Start
                    )
                }
                override fun pausePlayer() {
                    renderState(
                        PlayerState.Pause
                    )
                }
                override fun currentTimeMusic(timeInt: Int) {
                    renderState(
                        PlayerState.CurrentTime(
                            timeInt = timeInt
                        )
                    )
                }
            }
        )
    }

    private fun renderState(state: PlayerState) {
        stateLiveData.postValue(state)
    }

    fun pauseMediaPlayer() {
        workWithMediaPlayer.pauseMediaPlayer()
    }

    fun releseMediaPlayer(){
        workWithMediaPlayer.releseMediaPlayer()
    }

    fun playStartControl(){
        workWithMediaPlayer.playStartControl()
    }
}