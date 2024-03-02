package com.kuzmin.playlist.data.model

import android.content.Context
import android.net.Uri
import java.io.File

interface SaveFiles {
    fun saveImageToPrivateStorage(playlistName: String, uri: Uri, context: Context): File?
}