package com.kuzmin.playlist.domain.preferencesSearchHistory.iteractors

import com.kuzmin.playlist.domain.model.TrackDto

interface PreferencesSearchHistoryIteractor {
    fun getHistory(): ArrayList<TrackDto>
    fun saveHistory(trackList: List<TrackDto>)
    fun clearHistory()
    fun clearPreferences()
}