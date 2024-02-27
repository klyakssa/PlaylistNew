package com.kuzmin.playlist.presentation.library.Fragments.Playlist.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuzmin.playlist.domain.db.iterators.FavoriteIterator
import com.kuzmin.playlist.domain.db.iterators.PlaylistIterator
import com.kuzmin.playlist.presentation.library.Fragments.model.PlaylistState
import com.kuzmin.playlist.presentation.mapper.PlaylistMapper
import com.kuzmin.playlist.presentation.models.Playlist
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistIterator: PlaylistIterator
): ViewModel(){

    private val stateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = stateLiveData

    init {
        initUpdate()
    }

    private fun initUpdate() {
        playlistIterator.initListenerOnUpdate(
            object : PlaylistIterator.PlaylistListener {
                override fun callOnupdate() {
                    getPlaylists()
                }
            }
        )
    }

    fun getPlaylists() {
        viewModelScope.launch{
            playlistIterator
                .getPlaylists()
                .collect{playlists ->
                    processResult(playlists.map {
                        PlaylistMapper.map(it, )
                    })
                }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
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

}