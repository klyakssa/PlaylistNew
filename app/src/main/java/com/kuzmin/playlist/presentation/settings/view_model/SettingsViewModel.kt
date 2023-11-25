package com.kuzmin.playlist.presentation.settings.view_model

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kuzmin.playlist.R
import com.kuzmin.playlist.creator.Creator
import com.kuzmin.playlist.domain.preferencesTheme.iterators.PreferencesThemeIteractor
import com.kuzmin.playlist.domain.sharing.iterators.SharingInteractor
import com.kuzmin.playlist.domain.sharing.model.EmailData
import com.kuzmin.playlist.presentation.application.App

class SettingsViewModel(
    application: Application,
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: PreferencesThemeIteractor,
) : AndroidViewModel(application = application) {


    fun switchTheme(bool: Boolean) {
        settingsInteractor.setThemeToPreferences(bool)
    }

    fun getTheme(): Boolean{
       return settingsInteractor.getThemeFromPreferences()
    }
    fun shareApp(): Intent {
        return sharingInteractor.shareApp(getApplication<App>().getString(R.string.messageProfile))
    }

    fun openSupport(): Intent {
        return sharingInteractor.openSupport(
            email = EmailData(
            mailto = Uri.parse(getApplication<App>().getString(R.string.mailto)),
            email = getApplication<App>().getString(R.string.emailMy),
            subject = getApplication<App>().getString(R.string.subjectEmail),
            text = getApplication<App>().getString(R.string.textEmail),
            type = getApplication<App>().getString(R.string.type),
        )
        )
    }

    fun openTerms(): Intent {
        return sharingInteractor.openTerms(getApplication<App>().getString(R.string.termsLink))
    }


    companion object {
        const val PLAYLIST_PREFERENCES = "playlist_preferences"
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactorShare = (this[APPLICATION_KEY] as App).provideShareInteraction()
                val interactorTheme = Creator.providePreferencesThemeInteraction(context.getSharedPreferences(
                    PLAYLIST_PREFERENCES,
                    Application.MODE_PRIVATE
                ))
                SettingsViewModel(
                    (this[APPLICATION_KEY] as App),
                    interactorShare,
                    interactorTheme,
                )
            }
        }
    }
}