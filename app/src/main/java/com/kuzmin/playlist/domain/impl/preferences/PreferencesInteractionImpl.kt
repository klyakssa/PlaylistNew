package com.kuzmin.playlist.domain.impl.preferences

import com.kuzmin.playlist.domain.iterators.preferences.PreferencesIteractor
import com.kuzmin.playlist.domain.repository.preferences.PreferencesRepository

class PreferencesInteractionImpl(
    private val preferencesRepository: PreferencesRepository,
): PreferencesIteractor{
    override fun getThemeFromPreferences(): Boolean {
        return preferencesRepository.getThemeFromPreferences()
    }
    override fun setThemeToPreferences(bool: Boolean) {
        preferencesRepository.setThemeToPreferences(bool)
    }
}