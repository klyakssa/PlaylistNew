package com.kuzmin.playlist.presentation.library.Fragments.model

import com.kuzmin.playlist.domain.model.PlaylistDto

sealed interface PlaylistState{
    object isNotPlaylist : PlaylistState

    data class isPlaylist(
        val playlist: List<PlaylistDto>
    ) : PlaylistState
}