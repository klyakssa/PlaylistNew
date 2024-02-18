package com.kuzmin.playlist.presentation.library.Fragments.model

import com.kuzmin.playlist.domain.model.TrackDto


sealed interface FavoriteState{
    object isNotTracks : FavoriteState

    data class isTracks(
        val tracks: List<TrackDto>
    ) : FavoriteState
}