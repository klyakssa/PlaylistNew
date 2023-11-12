package com.kuzmin.playlist.data.repository

import android.app.AlertDialog
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.kuzmin.playlist.domain.model.Preferences
import com.kuzmin.playlist.domain.repository.PreferencesListener
import com.kuzmin.playlist.domain.repository.PreferencesRepository

class PreferencesRepositoryImpl(
    private val sp: SharedPreferences
): PreferencesRepository {
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

    override fun setListener(listener: PreferencesListener) {
        sp.registerOnSharedPreferenceChangeListener { _, _ ->
            listener.setChangePreferencesListener(getThemeFromPreferences())
            //(Creator.application as App).switchTheme(getThemeFromPreferences())
        }
    }

}