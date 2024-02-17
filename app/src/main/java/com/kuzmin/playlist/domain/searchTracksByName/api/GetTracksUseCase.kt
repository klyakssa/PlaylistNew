package com.kuzmin.playlist.domain.searchTracksByName.api

import com.kuzmin.playlist.domain.model.TrackDto
import com.kuzmin.playlist.domain.searchTracksByName.consumer.Consumer
import kotlinx.coroutines.flow.Flow

interface GetTracksUseCase {
    fun execute(trackName: String?): Flow<Pair<ArrayList<TrackDto>?, String?>>
}