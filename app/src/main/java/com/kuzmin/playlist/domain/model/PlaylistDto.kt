package com.kuzmin.playlist.domain.model


data class PlaylistDto(
    val playlistId: Int,
    val playlistName: String,
    val playlistDescribe: String,
    val imgFilePath: String,
    val idAddedTracks: String,
    val countTracks: Int
)
