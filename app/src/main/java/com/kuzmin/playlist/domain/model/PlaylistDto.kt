package com.kuzmin.playlist.domain.model

import java.io.File


data class PlaylistDto(
    val playlistId: Int,
    val playlistName: String,
    val playlistDescribe: String,
    val imgFilePath: File,
    val idAddedTracks: String,
    val countTracks: Int
)
