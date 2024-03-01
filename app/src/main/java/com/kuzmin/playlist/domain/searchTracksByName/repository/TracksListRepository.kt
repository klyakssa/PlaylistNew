package com.kuzmin.playlist.domain.searchTracksByName.repository

import com.kuzmin.playlist.domain.model.Resource
import com.kuzmin.playlist.data.model.TrackDto
import kotlinx.coroutines.flow.Flow

interface TracksListRepository {
    fun getTracks(trackName: String?): Flow<Resource<ArrayList<TrackDto>>>
}