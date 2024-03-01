package com.kuzmin.playlist.data.repository.PreferencesSearchHistory

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kuzmin.playlist.data.model.Preferences
import com.kuzmin.playlist.data.model.TrackDto
import com.kuzmin.playlist.domain.preferencesSearchHistory.repository.PreferencesSearchHistoryRepository

class PreferencesSearchHistoryRepositoryImpl(
    private val sp: SharedPreferences,
    private val gson: Gson
):PreferencesSearchHistoryRepository {
    override fun getHistory(): ArrayList<TrackDto> {
        val itemType = object : TypeToken<ArrayList<TrackDto>>() {}.type
        val json = sp.getString(Preferences.SEARCH_HISTORY_KEY.pref, null) ?: return ArrayList()
        return gson.fromJson<ArrayList<TrackDto>>(json, itemType)
    }

    override fun saveHistory(trackList: List<TrackDto>) {
        sp.edit()
            .putString(Preferences.SEARCH_HISTORY_KEY.pref, gson.toJson(trackList))
            .apply()
    }

    override fun clearHistory() {
        sp.edit()
            .remove(Preferences.SEARCH_HISTORY_KEY.pref)
            .apply()
    }

    override fun clearPreferences() {
        sp.edit()
            .clear()
            .apply()
    }
}