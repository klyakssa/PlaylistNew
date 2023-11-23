package com.kuzmin.playlist.domain.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.kuzmin.playlist.R
import com.kuzmin.playlist.creator.Creator
import com.kuzmin.playlist.domain.sharing.model.EmailData
import com.kuzmin.playlist.domain.sharing.iterators.SharingInteractor
import com.kuzmin.playlist.domain.sharing.repository.ExternalNavigator

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp(): Intent {
        return externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms(): Intent{
       return externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport(): Intent {
        return externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return Creator.app.getString(R.string.messageProfile)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            mailto = Uri.parse(Creator.app.getString(R.string.mailto)),
            email = Creator.app.getString(R.string.emailMy),
            subject = Creator.app.getString(R.string.subjectEmail),
            text = Creator.app.getString(R.string.textEmail),
            type = Creator.app.getString(R.string.type),
        )
    }

    private fun getTermsLink(): String {
        return Creator.app.getString(R.string.termsLink)
    }
}