package com.kuzmin.playlist.data.repository.PreferencesTheme

import android.app.Application
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import androidx.activity.ComponentDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.CoreComponentFactory
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