package com.kuzmin.playlist.presentation.audioplayer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kuzmin.playlist.R
import com.kuzmin.playlist.creator.Creator
import com.kuzmin.playlist.domain.mediaplayer.iterators.MediaPlayerIteractor
import com.kuzmin.playlist.domain.mediaplayer.repository.MediaPlayerRepository
import com.kuzmin.playlist.presentation.audioplayer.model.PlayerState
import com.kuzmin.playlist.presentation.mapper.DateTimeMapper
import com.kuzmin.playlist.presentation.search.model.TracksState
import com.kuzmin.playlist.presentation.settings.view_model.SettingsViewModel

class PlayerViewModel(
    private val url: String,
    private val workWithMediaPlayer: MediaPlayerIteractor,
): ViewModel() {

    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = stateLiveData

    companion object {
        fun getViewModelFactory(url: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val workWithMediaPlayer = Creator.provideMediaPlayerInteraction()
                PlayerViewModel(
                    url,
                    workWithMediaPlayer
                )
            }
        }
    }

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