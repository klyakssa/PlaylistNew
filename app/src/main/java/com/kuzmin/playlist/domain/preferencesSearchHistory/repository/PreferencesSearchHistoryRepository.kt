package com.kuzmin.playlist.domain.preferencesSearchHistory.repository

import com.kuzmin.playlist.data.model.TrackDto

interface PreferencesSearchHistoryRepository {
    fun getHistory(): ArrayList<TrackDto>
    fun saveHistory(trackList: List<TrackDto>)
    fun clearHistory()
    fun clearPreferences()
}