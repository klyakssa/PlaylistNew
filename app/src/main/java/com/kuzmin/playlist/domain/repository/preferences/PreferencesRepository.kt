package com.kuzmin.playlist.domain.repository.preferences

interface PreferencesRepository {
    fun getThemeFromPreferences() : Boolean
    fun setThemeToPreferences(bool: Boolean)
}