package com.kuzmin.playlist.domain.preferencesTheme.repository

interface PreferencesThemeRepository {
    fun getThemeFromPreferences() : Boolean
    fun setThemeToPreferences(bool: Boolean)
}