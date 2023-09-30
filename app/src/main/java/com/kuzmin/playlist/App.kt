package com.kuzmin.playlist

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(SettingsActivity.DARK_THEME, MODE_PRIVATE)
        val theme = sharedPrefs.getBoolean(SettingsActivity.DARK_THEME_KEY, false)
        if(theme){
            switchTheme(true)
        }else if(!theme){
            switchTheme(false)
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}