package com.kuzmin.playlist.presentation.settings.view_model

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
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
    private val context: Context,
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: PreferencesThemeIteractor,
) : ViewModel() {


    init {
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            switchTheme(true)
        }else{
            switchTheme(false)
        }
    }

    fun switchTheme(bool: Boolean) {
        settingsInteractor.setThemeToPreferences(bool)
    }

    fun getTheme(): Boolean{
       return settingsInteractor.getThemeFromPreferences()
    }
    fun shareApp(): Intent {
        return sharingInteractor.shareApp(context.getString(R.string.messageProfile))
    }

    fun openSupport(): Intent {
        return sharingInteractor.openSupport(
            email = EmailData(
            mailto = Uri.parse(context.getString(R.string.mailto)),
            email = context.getString(R.string.emailMy),
            subject = context.getString(R.string.subjectEmail),
            text = context.getString(R.string.textEmail),
            type = context.getString(R.string.type),
        )
        )
    }

    fun openTerms(): Intent {
        return sharingInteractor.openTerms(context.getString(R.string.termsLink))
    }

}