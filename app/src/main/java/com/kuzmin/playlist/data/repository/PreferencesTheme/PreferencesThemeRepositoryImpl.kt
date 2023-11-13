package com.kuzmin.playlist.data.repository.PreferencesTheme

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.kuzmin.playlist.domain.model.Preferences
import com.kuzmin.playlist.domain.preferencesTheme.repository.PreferencesThemeRepository

class PreferencesThemeRepositoryImpl(
    private val sp: SharedPreferences
): PreferencesThemeRepository {
    override fun getThemeFromPreferences(): Boolean{
        return sp.getBoolean(Preferences.DARK_THEME_KEY.pref, false)
    }

    override fun setThemeToPreferences(bool: Boolean) {
        sp.edit()
            .putBoolean(Preferences.DARK_THEME_KEY.pref, bool)
            .apply()
        AppCompatDelegate.setDefaultNightMode(
            if (bool) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}