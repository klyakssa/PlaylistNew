package com.kuzmin.playlist

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kuzmin.playlist.domain.model.Preferences


class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun savePreferences(trackList: ArrayList<String>) {
        sharedPreferences.edit()
            .putString(Preferences.SEARCH_HISTORY_KEY.pref, Gson().toJson(trackList))
            .apply()
    }

    fun readPreferences() : ArrayList<String>{
        val itemType = object : TypeToken<ArrayList<String>>() {}.type
        val json = sharedPreferences.getString(Preferences.SEARCH_HISTORY_KEY.pref, null) ?: return ArrayList()
        return Gson().fromJson<ArrayList<String>>(json, itemType)
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