package com.kuzmin.playlist.ui.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.kuzmin.playlist.domain.model.Preferences
import com.kuzmin.playlist.creator.Creator
import com.kuzmin.playlist.domain.repository.PreferencesListener

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        Creator.initPreferences(getSharedPreferences(Preferences.PLAYLIST_PREFERENCES.pref, MODE_PRIVATE))
        switchTheme(Creator.workWithPreferencesUseCase.getTheme())
    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}