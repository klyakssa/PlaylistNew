package com.kuzmin.playlist.presentation.library.Fragments.Playlist.AddNewPlaylist.models


sealed interface CreatePlaylistState{
    data class Success(
        val playlistName: String
    ) : CreatePlaylistState
    data class Error(
        val error: String
    ): CreatePlaylistState
}