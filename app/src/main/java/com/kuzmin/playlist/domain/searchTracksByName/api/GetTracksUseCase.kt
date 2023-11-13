package com.kuzmin.playlist.domain.searchTracksByName.api

import com.kuzmin.playlist.domain.model.TrackDto
import com.kuzmin.playlist.domain.searchTracksByName.consumer.Consumer

interface GetTracksUseCase {
    fun execute(
        trackName: String?,
                consumer: Consumer<ArrayList<TrackDto>>
    )
}