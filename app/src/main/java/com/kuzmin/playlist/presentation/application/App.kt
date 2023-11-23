package com.kuzmin.playlist.presentation.application

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.kuzmin.playlist.data.model.Preferences
import com.kuzmin.playlist.creator.Creator
import com.kuzmin.playlist.data.repository.PreferencesTheme.PreferencesThemeRepositoryImpl
import com.kuzmin.playlist.data.repository.share.ExternalNavigatorImpl
import com.kuzmin.playlist.domain.preferencesTheme.impl.PreferencesThemeInteractionImpl
import com.kuzmin.playlist.domain.preferencesTheme.iterators.PreferencesThemeIteractor
import com.kuzmin.playlist.domain.preferencesTheme.repository.PreferencesThemeRepository
import com.kuzmin.playlist.domain.sharing.impl.SharingInteractorImpl
import com.kuzmin.playlist.domain.sharing.iterators.SharingInteractor
import com.kuzmin.playlist.domain.sharing.repository.ExternalNavigator

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        Creator.initApp(baseContext)
        val workWithPreferencesUseCase = providePreferencesThemeInteraction()
        val currentNightMode = baseContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            workWithPreferencesUseCase.setThemeToPreferences(true)
        }else{
            workWithPreferencesUseCase.setThemeToPreferences(false)
        }
        switchTheme(workWithPreferencesUseCase.getThemeFromPreferences())
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

    private fun getPreferencesThemeRepository(sp: SharedPreferences): PreferencesThemeRepository {
        return PreferencesThemeRepositoryImpl(sp)
    }
    fun providePreferencesThemeInteraction(): PreferencesThemeIteractor {
        return PreferencesThemeInteractionImpl(getPreferencesThemeRepository(getSharedPreferences(
            Preferences.PLAYLIST_PREFERENCES.pref, MODE_PRIVATE)))
    }

    private fun getExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl()
    }
    fun provideShareInteraction(): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator())
    }
}