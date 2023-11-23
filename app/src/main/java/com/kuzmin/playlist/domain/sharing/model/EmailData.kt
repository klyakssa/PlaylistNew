package com.kuzmin.playlist.domain.sharing.model

import android.net.Uri

data class EmailData(
    val mailto: Uri,
    val email: String,
    val subject: String,
    val text: String,
    val type: String,
)
