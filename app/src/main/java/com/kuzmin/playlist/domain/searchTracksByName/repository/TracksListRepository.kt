package com.kuzmin.playlist.domain.searchTracksByName.repository

import com.kuzmin.playlist.domain.model.Resource
import com.kuzmin.playlist.domain.model.TrackDto

interface TracksListRepository {
    fun getTracks(trackName: String?): Resource<ArrayList<TrackDto>>
}