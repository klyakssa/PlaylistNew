package com.kuzmin.playlist.presentation.audioplayer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuzmin.playlist.domain.db.iterators.FavoriteIterator
import com.kuzmin.playlist.domain.mediaplayer.iterators.MediaPlayerIteractor
import com.kuzmin.playlist.domain.mediaplayer.repository.MediaPlayerRepository
import com.kuzmin.playlist.domain.model.TrackDto
import com.kuzmin.playlist.presentation.audioplayer.model.PlayerState
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val url: String,
    private val workWithMediaPlayer: MediaPlayerIteractor,
    private val favoriteIterator: FavoriteIterator,
): ViewModel() {

    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private var clickedLike: Boolean = false

    init {
        initPlayer()
    }

    fun initFav(trackId: String) {
        viewModelScope.launch {
            favoriteIterator.
                    existTrack(trackId)
                    .collect{
                        processResult(it)
                    }
        }
    }

    private fun processResult(like: Boolean) {
        if (like){
            clickedLike = true
            renderState(
                PlayerState.Liked
            )
        }else{
            clickedLike = false
            renderState(
                PlayerState.NotLiked
            )
        }
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

    fun addOrDelTrack(track: TrackDto){
        if (clickedLike){
            clickedLike = false
            viewModelScope.launch {
                favoriteIterator.deleteTrack(track)
            }
            renderState(
                PlayerState.NotLiked
            )
        }else{
            clickedLike = true
            viewModelScope.launch {
                favoriteIterator.insertTrack(track)
            }
            renderState(
                PlayerState.Liked
            )
        }

    }
}