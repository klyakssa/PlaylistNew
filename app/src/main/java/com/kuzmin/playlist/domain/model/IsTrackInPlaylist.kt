package com.kuzmin.playlist.domain.model

sealed interface IsTrackInPlaylist{
    data class isInPlaylist(
        val playlistName: String
    ): IsTrackInPlaylist
    data class notInPlaylist(
        val playlistName: String
    ): IsTrackInPlaylist
}