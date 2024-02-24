package com.kuzmin.playlist.presentation.audioplayer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuzmin.playlist.domain.db.iterators.FavoriteIterator
import com.kuzmin.playlist.domain.db.iterators.PlaylistIterator
import com.kuzmin.playlist.domain.mediaplayer.iterators.MediaPlayerIteractor
import com.kuzmin.playlist.domain.mediaplayer.repository.MediaPlayerRepository
import com.kuzmin.playlist.domain.model.IsTrackInPlaylist
import com.kuzmin.playlist.domain.model.PlaylistDto
import com.kuzmin.playlist.domain.model.TrackDto
import com.kuzmin.playlist.presentation.audioplayer.model.PlayerState
import com.kuzmin.playlist.presentation.audioplayer.model.UpdateLibrary
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val url: String,
    private val workWithMediaPlayer: MediaPlayerIteractor,
    private val favoriteIterator: FavoriteIterator,
    private val updateLibrary: UpdateLibrary,
    private val playlistIterator: PlaylistIterator,
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
                    .collect{like ->
                        processResult(like)
                    }
        }
    }

    private fun processResult(like: Boolean) {
        clickedLike = like
        renderState(if (like) PlayerState.Liked else PlayerState.NotLiked)
    }

    fun getPlaylists() {
        viewModelScope.launch{
            playlistIterator
                .getPlaylists()
                .collect{ playlists ->
                    processResult(playlists)
                }
        }
    }

    private fun processResult(playlists: List<PlaylistDto>) {
        if (playlists.isNullOrEmpty()){
            renderState(
                PlayerState.isNotPlaylist
            )
        }else{
            renderState(
                PlayerState.isPlaylist(
                    playlists
                )
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
        viewModelScope.launch {
            updateLibrary.callOnUpdate()
        }
    }

    fun addTrackInPlaylist(playlist: PlaylistDto, trackId: String){
       viewModelScope.launch {
           playlistIterator
               .updateTracksInPlaylist(playlist, trackId)
               .collect{
                   processResult(it)
               }
       }
    }

    private fun processResult(it: IsTrackInPlaylist) {
        when(it){
            is IsTrackInPlaylist.isInPlaylist -> {
                renderState(
                   PlayerState.isInPlaylist(
                       it.playlistName
                   )
                )
            }
            is IsTrackInPlaylist.notInPlaylist -> {
                renderState(
                    PlayerState.notInPlaylist(
                        it.playlistName
                    )
                )
            }
        }
    }
}