package com.kuzmin.playlist

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(Const.PLAYLIST_PREFERENCES.const, MODE_PRIVATE)
        val theme = sharedPrefs.getBoolean(Const.DARK_THEME_KEY.const, false)
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