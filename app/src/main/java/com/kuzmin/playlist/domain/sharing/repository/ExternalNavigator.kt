package com.kuzmin.playlist.domain.sharing.repository

import android.content.Context
import android.content.Intent
import com.kuzmin.playlist.domain.sharing.model.EmailData

interface ExternalNavigator {
    fun shareLink(link: String): Intent
    fun openLink(link: String): Intent
    fun openEmail(email: EmailData): Intent//В задании было сказанно что использовать надо свою почту, а куда написать использоваться не должно, или прикрепите задание по intents, может я что то не увидел
}