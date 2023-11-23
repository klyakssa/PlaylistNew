package com.kuzmin.playlist.presentation.audioplayer.model

import com.kuzmin.playlist.domain.model.TrackDto
import com.kuzmin.playlist.presentation.search.model.TracksState

sealed interface PlayerState{
    object Prepared : PlayerState
    object Completion : PlayerState
    object Start : PlayerState
    object Pause : PlayerState
    data class CurrentTime(
        val timeInt: Int
    ) : PlayerState
}