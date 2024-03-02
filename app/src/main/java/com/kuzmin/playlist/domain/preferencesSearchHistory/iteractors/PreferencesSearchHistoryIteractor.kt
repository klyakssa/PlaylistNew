package com.kuzmin.playlist.domain.preferencesSearchHistory.iteractors

import com.kuzmin.playlist.data.model.TrackDto

interface PreferencesSearchHistoryIteractor {
    fun getHistory(): ArrayList<TrackDto>
    fun saveHistory(trackList: List<TrackDto>)
    fun clearHistory()
    fun clearPreferences()
}