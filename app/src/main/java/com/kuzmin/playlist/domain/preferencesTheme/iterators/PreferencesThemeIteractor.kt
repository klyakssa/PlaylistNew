package com.kuzmin.playlist.domain.preferencesTheme.iterators

interface PreferencesThemeIteractor {
    fun getThemeFromPreferences() : Boolean
    fun setThemeToPreferences(bool: Boolean)
}