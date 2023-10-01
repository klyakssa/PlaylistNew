package com.kuzmin.playlist

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kuzmin.playlist.trackList.Track


class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun savePreferences(trackList: ArrayList<Track>) {
        sharedPreferences.edit()
            .putString(Const.SEARCH_HISTORY_KEY.const, Gson().toJson(trackList))
            .apply()
    }

    fun readPreferences() : ArrayList<Track>{
        val itemType = object : TypeToken<ArrayList<Track>>() {}.type
        val json = sharedPreferences.getString(Const.SEARCH_HISTORY_KEY.const, null) ?: return ArrayList()
        return Gson().fromJson<ArrayList<Track>>(json, itemType)
    }

    fun clearHistory(){
        sharedPreferences.edit()
            .remove(Const.SEARCH_HISTORY_KEY.const)
            .apply()
    }

    fun clearPreferences(){
        sharedPreferences.edit()
            .clear()
            .apply()
    }
}