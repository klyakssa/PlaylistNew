package com.kuzmin.playlist.presentation.application

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.kuzmin.playlist.data.model.Preferences
import com.kuzmin.playlist.di.dataModule
import com.kuzmin.playlist.di.interactorModule
import com.kuzmin.playlist.di.repositoryModule
import com.kuzmin.playlist.di.useCaseModule
import com.kuzmin.playlist.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule, useCaseModule)
        }
        switchTheme(getSharedPreferences("playlist_preferences",
            MODE_PRIVATE
        ).getBoolean(Preferences.DARK_THEME_KEY.pref, false))
    }

    fun switchTheme(darkThemeEnabled: Boolean){
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}