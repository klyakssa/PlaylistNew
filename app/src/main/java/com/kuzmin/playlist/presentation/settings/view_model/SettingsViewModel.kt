package com.kuzmin.playlist.presentation.settings.view_model

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kuzmin.playlist.creator.Creator
import com.kuzmin.playlist.data.model.Preferences
import com.kuzmin.playlist.domain.preferencesTheme.iterators.PreferencesThemeIteractor
import com.kuzmin.playlist.domain.sharing.iterators.SharingInteractor
import com.kuzmin.playlist.presentation.application.App

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: PreferencesThemeIteractor,
) : ViewModel() {


    fun switchTheme(bool: Boolean) {
        settingsInteractor.setThemeToPreferences(bool)
    }

    fun getTheme(): Boolean{
       return settingsInteractor.getThemeFromPreferences()
    }
    fun shareApp(): Intent {
        return sharingInteractor.shareApp()
    }

    fun openSupport(): Intent {
        return sharingInteractor.openSupport()
    }

    fun openTerms(): Intent {
        return sharingInteractor.openTerms()
    }


    companion object {
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactorShare = (this[APPLICATION_KEY] as App).provideShareInteraction()
                val interactorTheme = Creator.providePreferencesThemeInteraction(context.getSharedPreferences(
                    Preferences.PLAYLIST_PREFERENCES.pref,
                    Application.MODE_PRIVATE
                ))
                SettingsViewModel(
                    interactorShare,
                    interactorTheme,
                )
            }
        }
    }
}