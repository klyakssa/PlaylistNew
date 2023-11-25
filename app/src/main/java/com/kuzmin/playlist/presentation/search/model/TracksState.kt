package com.kuzmin.playlist.presentation.search.model

import com.kuzmin.playlist.domain.model.TrackDto

sealed interface TracksState {

    object Loading : TracksState

    object Start : TracksState

    data class Content(
        val tracks: List<TrackDto>
    ) : TracksState

    data class Error(
        val errorMessage: String
    ) : TracksState

    data class Empty(
        val message: String
    ) : TracksState

    data class ShowHistory(
        val tracks: ArrayList<TrackDto>
    ) : TracksState

}