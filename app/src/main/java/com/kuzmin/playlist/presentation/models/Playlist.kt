package com.kuzmin.playlist.presentation.models

import java.io.File


data class Playlist(
    val playlistId: Int,
    val playlistName: String,
    val playlistDescribe: String,
    val imgFilePath: File,
    val idAddedTracks: String,
    val countTracks: Int
)
