package com.kuzmin.playlist.presentation.library.Fragments.Favorite.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuzmin.playlist.domain.db.iterators.FavoriteIterator
import com.kuzmin.playlist.domain.model.TrackDto
import com.kuzmin.playlist.presentation.library.Fragments.model.FavoriteState
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val favoriteIterator: FavoriteIterator,
):  ViewModel(){

    private val stateLiveData = MutableLiveData<FavoriteState>()
    fun observeState(): LiveData<FavoriteState> = stateLiveData

    fun initTracks() {
        viewModelScope.launch{
            favoriteIterator
                .getTracks()
                .collect{
                    processResult(it)
                }
        }
    }

    private fun processResult(tracks: List<TrackDto>) {
        if (tracks.isNullOrEmpty()){
            renderState(
                FavoriteState.isNotTracks
            )
        }else{
            renderState(
                FavoriteState.isTracks(
                    tracks
                )
            )
        }
    }

    private fun renderState(state: FavoriteState) {
        stateLiveData.postValue(state)
    }
}