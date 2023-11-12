package com.kuzmin.playlist.domain.iterators.preferences

interface PreferencesIteractor {
    fun getThemeFromPreferences() : Boolean
    fun setThemeToPreferences(bool: Boolean)
}