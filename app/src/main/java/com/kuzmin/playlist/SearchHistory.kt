package com.kuzmin.playlist

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kuzmin.playlist.domain.model.Preferences
import com.kuzmin.playlist.domain.model.TrackDto


class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun savePreferences(trackList: ArrayList<TrackDto>) {
        sharedPreferences.edit()
            .putString(Preferences.SEARCH_HISTORY_KEY.pref, Gson().toJson(trackList))
            .apply()
    }

    fun readPreferences() : ArrayList<TrackDto>{
        val itemType = object : TypeToken<ArrayList<TrackDto>>() {}.type
        val json = sharedPreferences.getString(Preferences.SEARCH_HISTORY_KEY.pref, null) ?: return ArrayList()
        return Gson().fromJson<ArrayList<TrackDto>>(json, itemType)
    }

    fun clearHistory(){
        sharedPreferences.edit()
            .remove(Preferences.SEARCH_HISTORY_KEY.pref)
            .apply()
    }

    fun clearPreferences(){
        sharedPreferences.edit()
            .clear()
            .apply()
    }
}