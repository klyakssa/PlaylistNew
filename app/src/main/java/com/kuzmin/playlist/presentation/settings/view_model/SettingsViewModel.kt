package com.kuzmin.playlist.presentation.settings.view_model

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.kuzmin.playlist.R
import com.kuzmin.playlist.domain.preferencesTheme.iterators.PreferencesThemeIteractor
import com.kuzmin.playlist.domain.sharing.iterators.SharingInteractor
import com.kuzmin.playlist.domain.sharing.model.EmailData

class SettingsViewModel(
    private val context: Context,
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: PreferencesThemeIteractor,
) : ViewModel() {

    fun switchTheme(bool: Boolean) {
        settingsInteractor.setThemeToPreferences(bool)
    }

    fun getTheme(): Boolean {
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