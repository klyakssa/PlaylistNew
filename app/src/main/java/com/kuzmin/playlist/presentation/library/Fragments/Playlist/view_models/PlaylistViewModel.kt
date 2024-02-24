package com.kuzmin.playlist.presentation.library.Fragments.Playlist.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuzmin.playlist.domain.db.iterators.PlaylistIterator
import com.kuzmin.playlist.domain.model.PlaylistDto
import com.kuzmin.playlist.presentation.library.Fragments.model.PlaylistState
import com.kuzmin.playlist.presentation.library.Fragments.model.UpdatePlaylist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistViewModel(
    private val playlistIterator: PlaylistIterator
): ViewModel(), UpdatePlaylist{

    private val stateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = stateLiveData

    fun getPlaylists() {
        viewModelScope.launch{
            playlistIterator
                .getPlaylists()
                .collect{playlists ->
                    processResult(playlists)
                }
        }
    }

    private fun processResult(playlists: List<PlaylistDto>) {
        if (playlists.isNullOrEmpty()){
            renderState(
                PlaylistState.isNotPlaylist
            )
        }else{
            renderState(
                PlaylistState.isPlaylist(
                    playlists
                )
            )
        }
    }

    private fun renderState(state: PlaylistState) {
        stateLiveData.postValue(state)
    }

    override suspend fun callOnUpdatePlaylist() {
        getPlaylists()
    }
}