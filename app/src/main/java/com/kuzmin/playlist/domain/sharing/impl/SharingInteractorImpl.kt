package com.kuzmin.playlist.domain.sharing.impl


import android.content.Intent
import com.kuzmin.playlist.domain.sharing.model.EmailData
import com.kuzmin.playlist.domain.sharing.iterators.SharingInteractor
import com.kuzmin.playlist.domain.sharing.repository.ExternalNavigator

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp(link: String): Intent {
        return externalNavigator.shareLink(link)
    }

    override fun openTerms(link: String): Intent{
       return externalNavigator.openLink(link)
    }

    override fun openSupport(email: EmailData): Intent {
        return externalNavigator.openEmail(email)
    }
}