package com.kuzmin.playlist.domain.preferencesTheme.impl

import com.kuzmin.playlist.domain.preferencesTheme.iterators.PreferencesThemeIteractor
import com.kuzmin.playlist.domain.preferencesTheme.repository.PreferencesThemeRepository

class PreferencesThemeInteractionImpl(
    private val preferencesRepository: PreferencesThemeRepository,
): PreferencesThemeIteractor {
    override fun getThemeFromPreferences(): Boolean {
        return preferencesRepository.getThemeFromPreferences()
    }
    override fun setThemeToPreferences(bool: Boolean) {
        preferencesRepository.setThemeToPreferences(bool)
    }
}