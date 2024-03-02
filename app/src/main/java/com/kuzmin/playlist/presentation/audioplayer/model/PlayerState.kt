package com.kuzmin.playlist.presentation.audioplayer.model

import com.kuzmin.playlist.presentation.models.Playlist


sealed interface PlayerState{
    object Liked : PlayerState
    object NotLiked : PlayerState
    object Prepared : PlayerState
    object Completion : PlayerState
    object Start : PlayerState
    object Pause : PlayerState
    object isNotPlaylist : PlayerState
    data class isPlaylist(
        val playlist: List<Playlist>
    ) : PlayerState
    data class CurrentTime(
        val timeInt: Int
    ) : PlayerState

    data class isInPlaylist(
        val playlistName: String
    ): PlayerState
    data class notInPlaylist(
        val playlistName: String
    ): PlayerState
}