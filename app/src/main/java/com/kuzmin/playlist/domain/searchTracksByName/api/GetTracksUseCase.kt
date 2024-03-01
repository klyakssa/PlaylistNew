package com.kuzmin.playlist.domain.searchTracksByName.api

import com.kuzmin.playlist.data.model.TrackDto
import kotlinx.coroutines.flow.Flow

interface GetTracksUseCase {
    fun execute(trackName: String?): Flow<Pair<ArrayList<TrackDto>?, String?>>
}