package com.kuzmin.playlist.domain.preferencesSearchHistory.repository

import com.kuzmin.playlist.domain.model.TrackDto

interface PreferencesSearchHistoryRepository {
    fun getHistory(): ArrayList<TrackDto>
    fun saveHistory(trackList: ArrayList<TrackDto>)
    fun clearHistory()
    fun clearPreferences()
}