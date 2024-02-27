package com.kuzmin.playlist.presentation.library.Fragments.model

import com.kuzmin.playlist.presentation.models.Track


sealed interface FavoriteState{
    object isNotTracks : FavoriteState

    data class isTracks(
        val tracks: List<Track>
    ) : FavoriteState
}