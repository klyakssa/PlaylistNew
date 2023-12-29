package com.kuzmin.playlist.presentation.search.view_model

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuzmin.playlist.R
import com.kuzmin.playlist.domain.model.TrackDto
import com.kuzmin.playlist.domain.preferencesSearchHistory.iteractors.PreferencesSearchHistoryIteractor
import com.kuzmin.playlist.domain.searchTracksByName.api.GetTracksUseCase
import com.kuzmin.playlist.domain.searchTracksByName.consumer.Consumer
import com.kuzmin.playlist.presentation.search.model.TracksState

class TracksSearchViewModel(
    private val context: Context,
    private val searchHistory: PreferencesSearchHistoryIteractor,
    private val tracksInteractor: GetTracksUseCase
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val handler = Handler(Looper.getMainLooper())

    private val tracksListHistory = ArrayList<TrackDto>()

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    private var latestSearchText: String? = null
    private var errorInet: Boolean = false

    init {
        getHistory()
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText && !errorInet) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TracksState.Loading)
            tracksInteractor.execute(newSearchText, object : Consumer<ArrayList<TrackDto>> {
                override fun consume(
                    data: ArrayList<TrackDto>?,
                    errorMessage: String?
                ) {
                    val tracks = mutableListOf<TrackDto>()
                    if (data != null) {
                        tracks.addAll(data)
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
            })
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
                TracksState.Loading
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