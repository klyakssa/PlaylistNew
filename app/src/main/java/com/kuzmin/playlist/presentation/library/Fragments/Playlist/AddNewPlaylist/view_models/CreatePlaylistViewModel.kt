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
import com.kuzmin.playlist.presentation.library.Fragments.model.UpdatePlaylist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistViewModel(
    private val activity: FragmentActivity,
    private val playlistIterator: PlaylistIterator,
    private val updatePlaylist: UpdatePlaylist,
): ViewModel() {

    private val stateLiveData = MutableLiveData<CreatePlaylistState>()
    fun observeState(): LiveData<CreatePlaylistState> = stateLiveData

    fun createPlaylist(playlistName: String, playlistDescribe: String, imgFilePath: String) {
        viewModelScope.launch {
            playlistIterator.insertPlaylist(playlistName, playlistDescribe, imgFilePath)
                .collect{ error ->
                    processResult(error, playlistName, imgFilePath)
                }
        }

    }

    private fun processResult(error: String?, playlistName: String, imgFilePath: String) {
        if (error != null){
            renderState(
                CreatePlaylistState.Error(error)
            )
        }
        if (!imgFilePath.isEmpty()) {
            val error = saveImageToPrivateStorage(playlistName, Uri.parse(imgFilePath))
            if (error != null) {
                renderState(
                    CreatePlaylistState.Error(error.toString())
                )
            }
        }
        viewModelScope.launch{
            updatePlaylist.callOnUpdatePlaylist()
        }
        renderState(
            CreatePlaylistState.Success(playlistName)
        )
    }

    private fun renderState(state: CreatePlaylistState) {
        stateLiveData.postValue(state)
    }

    private fun saveImageToPrivateStorage(playlistName: String, uri: Uri): Exception? {
        try {
            val filePath = File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "PlaylistMaker")
            if (!filePath.exists()){
                filePath.mkdirs()
            }
            val file = File(filePath, "${playlistName}.jpg")
            val inputStream = activity.applicationContext.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        } catch (e: Exception) {
            return e
        }
        return null
    }

}