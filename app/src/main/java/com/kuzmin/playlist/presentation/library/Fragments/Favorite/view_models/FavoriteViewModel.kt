package com.kuzmin.playlist.presentation.library.Fragments.Favorite.view_models

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuzmin.playlist.domain.db.iterators.FavoriteIterator
import com.kuzmin.playlist.presentation.library.Fragments.model.FavoriteState
import com.kuzmin.playlist.presentation.mapper.TrackMapper
import com.kuzmin.playlist.presentation.models.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val favoriteIterator: FavoriteIterator,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavoriteState>()

    private var isClickAllowed = true

    fun observeState(): LiveData<FavoriteState> = stateLiveData

    init {
        initUpdate()
    }

    private fun initUpdate() {
        favoriteIterator.initListenerOnUpdate(
            object : FavoriteIterator.FavoriteListener {
                override fun callOnupdate() {
                    getTracks()
                }
            }
        )
    }

    fun getTracks() {
        viewModelScope.launch {
            favoriteIterator
                .getTracks()
                .collect { tracks ->
                    processResult(tracks.map { trackDto ->
                        TrackMapper.map(trackDto)
                    })
                }
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isNullOrEmpty()) {
            renderState(
                FavoriteState.isNotTracks
            )
        } else {
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

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}