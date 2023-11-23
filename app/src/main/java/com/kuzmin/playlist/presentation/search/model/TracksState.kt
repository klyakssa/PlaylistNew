package com.kuzmin.playlist.presentation.search.model

import com.kuzmin.playlist.domain.model.TrackDto

sealed interface TracksState {

    object Loading : TracksState

    data class Content(
        val movies: List<TrackDto>
    ) : TracksState

    data class Error(
        val errorMessage: String
    ) : TracksState

    data class Empty(
        val message: String
    ) : TracksState

}