package com.kuzmin.playlist.domain.preferencesSearchHistory.impl

import com.kuzmin.playlist.domain.model.TrackDto
import com.kuzmin.playlist.domain.preferencesSearchHistory.iteractors.PreferencesSearchHistoryIteractor
import com.kuzmin.playlist.domain.preferencesSearchHistory.repository.PreferencesSearchHistoryRepository

class PreferencesSearchHistoryInteractionImpl(
    private val preferencesRepository: PreferencesSearchHistoryRepository
): PreferencesSearchHistoryIteractor{
    override fun getHistory(): ArrayList<TrackDto> {
        return preferencesRepository.getHistory()
    }

    override fun saveHistory(trackList: ArrayList<TrackDto>) {
        preferencesRepository.saveHistory(trackList)
    }

    override fun clearHistory() {
        preferencesRepository.clearHistory()
    }

    override fun clearPreferences() {
        preferencesRepository.clearPreferences()
    }
}