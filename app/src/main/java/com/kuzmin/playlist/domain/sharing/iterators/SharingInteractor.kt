package com.kuzmin.playlist.domain.sharing.iterators

import android.content.Intent
import com.kuzmin.playlist.domain.sharing.model.EmailData

interface SharingInteractor {
    fun shareApp(link:String): Intent
    fun openTerms(link:String): Intent
    fun openSupport(email:EmailData): Intent
}