package com.kuzmin.playlist.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.kuzmin.playlist.R
import com.kuzmin.playlist.data.model.SaveFiles
import java.io.File
import java.io.FileOutputStream

class SaveFilesImpl() : SaveFiles {
    override fun saveImageToPrivateStorage(playlistName: String, uri: Uri, context: Context): File? {
        try {
            val filePath =
                File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), context.getString(R.string.PlaylistMaker))
            if (!filePath.exists()) {
                filePath.mkdirs()
            }
            val file = File(filePath, "${playlistName}.jpg")
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
            return file
        } catch (e: Exception) {
            return null
        }
    }
}