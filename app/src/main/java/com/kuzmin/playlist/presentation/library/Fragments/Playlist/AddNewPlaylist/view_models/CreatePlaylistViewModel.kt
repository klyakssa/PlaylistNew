package com.kuzmin.playlist.presentation.library.Fragments.Playlist.AddNewPlaylist.view_models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuzmin.playlist.domain.db.iterators.PlaylistIterator
import com.kuzmin.playlist.presentation.library.Fragments.Playlist.AddNewPlaylist.models.CreatePlaylistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistViewModel(
    private val playlistIterator: PlaylistIterator,
): ViewModel() {

    private val stateLiveData = MutableLiveData<CreatePlaylistState>()
    fun observeState(): LiveData<CreatePlaylistState> = stateLiveData

    fun createPlaylist(playlistName: String, playlistDescribe: String, imgFilePath: String) {
        viewModelScope.launch {
            playlistIterator.insertPlaylist(playlistName, playlistDescribe, imgFilePath)
                .collect{ error ->
                    processResult(error, playlistName)
                }
        }

    }

    private fun processResult(error: String?, playlistName: String) {
        if (error != null){
            renderState(
                CreatePlaylistState.Error(error)
            )
        }
        renderState(
            CreatePlaylistState.Success(playlistName)
        )
    }

    private fun renderState(state: CreatePlaylistState) {
        stateLiveData.postValue(state)
    }

}