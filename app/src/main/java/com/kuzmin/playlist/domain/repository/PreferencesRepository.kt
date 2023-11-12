package com.kuzmin.playlist.domain.repository

import android.content.SharedPreferences.OnSharedPreferenceChangeListener

interface PreferencesRepository {
    fun getThemeFromPreferences() : Boolean
    fun setThemeToPreferences(bool: Boolean)
}