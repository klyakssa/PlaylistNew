package com.kuzmin.playlist.presentation.application

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.kuzmin.playlist.domain.model.Preferences
import com.kuzmin.playlist.creator.Creator

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        val workWithPreferencesUseCase = Creator.providePreferencesThemeInteraction(getSharedPreferences(Preferences.PLAYLIST_PREFERENCES.pref, MODE_PRIVATE))
        val currentNightMode = baseContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            workWithPreferencesUseCase.setThemeToPreferences(true)
        }else{
            workWithPreferencesUseCase.setThemeToPreferences(false)
        }
        switchTheme(workWithPreferencesUseCase.getThemeFromPreferences())
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