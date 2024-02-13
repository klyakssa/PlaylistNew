package com.kuzmin.playlist.presentation.search.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuzmin.playlist.R
import com.kuzmin.playlist.domain.model.TrackDto
import com.kuzmin.playlist.domain.preferencesSearchHistory.iteractors.PreferencesSearchHistoryIteractor
import com.kuzmin.playlist.domain.searchTracksByName.api.GetTracksUseCase
import com.kuzmin.playlist.domain.searchTracksByName.consumer.Consumer
import com.kuzmin.playlist.presentation.search.model.TracksState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TracksSearchViewModel(
    private val context: Context,
    private val searchHistory: PreferencesSearchHistoryIteractor,
    private val tracksInteractor: GetTracksUseCase
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }


    private var searchJob: Job? = null

    private val tracksListHistory = ArrayList<TrackDto>()

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    private var latestSearchText: String? = null
    private var errorInet: Boolean = false

    init {
        getHistory()
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText && !errorInet) {
            return
        }

        this.latestSearchText = changedText

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {

            renderState(TracksState.Loading)

            viewModelScope.launch {
                tracksInteractor
                    .execute(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundNames: ArrayList<TrackDto>?, errorMessage: String?) {
        val tracks = mutableListOf<TrackDto>()

        if (foundNames != null) {
            tracks.addAll(foundNames)
        }

        when {
            errorMessage != null -> {
                renderState(
                    TracksState.Error(
                        errorMessage = context.getString(R.string.something_went_wrong),
                    )
                )
                errorInet = true
            }
            tracks.isEmpty() -> {
                renderState(
                    TracksState.Empty(
                        message = context.getString(R.string.nothing_found),
                    )
                )
                errorInet = false
            }
            else -> {
                renderState(
                    TracksState.Content(
                        tracks = tracks,
                    )
                )
                errorInet = false
            }
        }
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    fun showHistory(changedText: String, hasFocus: Boolean) {
        if (changedText.isEmpty() && tracksListHistory.isNotEmpty() && hasFocus){
            renderState(
                TracksState.ShowHistory(tracksListHistory)
            )
        }else{
            renderState(
                TracksState.Start
            )
        }
    }

    private fun getHistory(){
        tracksListHistory.addAll(searchHistory.getHistory())
        renderState(
            TracksState.Start
        )
    }
    fun saveHistory(){
        searchHistory.saveHistory(tracksListHistory)
    }
    fun clearHistory() {
        searchHistory.clearHistory()
        renderState(
            TracksState.Start
        )
    }



    fun clickOnMainTrack(track: TrackDto){
        tracksListHistory.remove(track)
        tracksListHistory.add(0, track)
        if (tracksListHistory.size > 10) {
            tracksListHistory.subList(10, tracksListHistory.size).clear()
        }
    }

    fun clickOnHistoryTrack(track: TrackDto){
        tracksListHistory.remove(track)
        tracksListHistory.add(0, track)
        renderState(
            TracksState.ShowHistory(tracksListHistory)
        )
    }

}