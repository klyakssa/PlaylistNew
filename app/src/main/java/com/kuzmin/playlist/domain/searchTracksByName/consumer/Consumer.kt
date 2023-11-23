package com.kuzmin.playlist.domain.searchTracksByName.consumer

import com.kuzmin.playlist.domain.model.TrackDto

interface Consumer<T> {
    fun consume(data: ArrayList<TrackDto>?, errorMessage: String?)
}