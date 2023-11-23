package com.kuzmin.playlist.presentation.search.view_model

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kuzmin.playlist.R
import com.kuzmin.playlist.creator.Creator
import com.kuzmin.playlist.domain.model.Preferences
import com.kuzmin.playlist.domain.model.Preferences.*
import com.kuzmin.playlist.domain.model.TrackDto
import com.kuzmin.playlist.domain.preferencesSearchHistory.iteractors.PreferencesSearchHistoryIteractor
import com.kuzmin.playlist.domain.searchTracksByName.consumer.Consumer
import com.kuzmin.playlist.domain.searchTracksByName.consumer.ConsumerData
import com.kuzmin.playlist.presentation.application.App
import com.kuzmin.playlist.presentation.search.model.TracksState
import java.security.PrivateKey

class TracksSearchViewModel(
    application: Application,
    private val searchHistory: PreferencesSearchHistoryIteractor,
) : AndroidViewModel(application) {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val searchHistory = Creator.providePreferencesSearchHistoryInteraction((this[APPLICATION_KEY] as App).getSharedPreferences(
                    PLAYLIST_PREFERENCES.pref, MODE_PRIVATE
                ))
                TracksSearchViewModel(
                    this[APPLICATION_KEY] as App,
                    searchHistory,
                )
            }
        }
    }


    private val tracksInteractor = Creator.provideGetTracksListUseCase(getApplication())
    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    private var latestSearchText: String? = null

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
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
                    val movies = mutableListOf<TrackDto>()
                    if (data != null) {
                        movies.addAll(data)
                    }

                    when {
                        errorMessage != null -> {
                            renderState(
                                TracksState.Error(
                                    errorMessage = getApplication<App>().getString(R.string.something_went_wrong),
                                )
                            )
                        }
                        movies.isEmpty() -> {
                            renderState(
                                TracksState.Empty(
                                    message = getApplication<Application>().getString(R.string.nothing_found),
                                )
                            )
                        }

                        else -> {
                            renderState(
                                TracksState.Content(
                                    movies = movies,
                                )
                            )
                        }
                    }
                }
            })
        }
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    fun getHistory(): ArrayList<TrackDto>{
        return searchHistory.getHistory()
    }
    fun saveHistory(trackList: ArrayList<TrackDto>){
        searchHistory.saveHistory(trackList)
    }
    fun clearHistory() {
        searchHistory.clearHistory()
    }

}