package com.kuzmin.playlist.presentation.library.Fragments.model

import com.kuzmin.playlist.presentation.models.Playlist


sealed interface PlaylistState{
    object isNotPlaylist : PlaylistState

    data class isPlaylist(
        val playlist: List<Playlist>
    ) : PlaylistState
}