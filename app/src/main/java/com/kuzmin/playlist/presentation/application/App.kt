package com.kuzmin.playlist.presentation.application

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.kuzmin.playlist.creator.Creator
import com.kuzmin.playlist.data.repository.share.ExternalNavigatorImpl
import com.kuzmin.playlist.domain.sharing.impl.SharingInteractorImpl
import com.kuzmin.playlist.domain.sharing.iterators.SharingInteractor
import com.kuzmin.playlist.domain.sharing.repository.ExternalNavigator
import com.kuzmin.playlist.presentation.settings.view_model.SettingsViewModel

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        Creator.initApp(baseContext)
        val workWithPreferencesUseCase = Creator.providePreferencesThemeInteraction(getSharedPreferences(
            SettingsViewModel.PLAYLIST_PREFERENCES,
            MODE_PRIVATE
        ))
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

    private fun getExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl()
    }
    fun provideShareInteraction(): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator())
    }
}