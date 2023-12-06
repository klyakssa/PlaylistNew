package com.kuzmin.playlist.data.repository.share

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.kuzmin.playlist.R
import com.kuzmin.playlist.domain.sharing.model.EmailData
import com.kuzmin.playlist.domain.sharing.repository.ExternalNavigator

class ExternalNavigatorImpl: ExternalNavigator {
    override fun shareLink(link: String): Intent {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plane"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        return shareIntent
    }

    override fun openLink(link: String): Intent {
        val webIntent: Intent = Uri.parse(link).let { webpage ->
            Intent(Intent.ACTION_VIEW, webpage)
        }
        return webIntent
    }

    override fun openEmail(email: EmailData): Intent{
        return Intent().apply {
            action = Intent.ACTION_SEND
            data = email.mailto
            putExtra(Intent.EXTRA_EMAIL, email.email)
            putExtra(Intent.EXTRA_SUBJECT, email.subject)
            putExtra(Intent.EXTRA_TEXT, email.text)
            type = email.type
        }
    }
}