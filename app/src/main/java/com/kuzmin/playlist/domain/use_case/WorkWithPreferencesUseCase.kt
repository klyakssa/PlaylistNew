package com.kuzmin.playlist.domain.use_case

import android.content.SharedPreferences
import com.kuzmin.playlist.domain.repository.PreferencesListener
import com.kuzmin.playlist.domain.repository.PreferencesRepository

class WorkWithPreferencesUseCase(
    private val preferencesRepository: PreferencesRepository,
) {
    fun getTheme(): Boolean {
        return preferencesRepository.getThemeFromPreferences()
    }

    fun setTheme(bool: Boolean) {
        preferencesRepository.setThemeToPreferences(bool)
    }

}