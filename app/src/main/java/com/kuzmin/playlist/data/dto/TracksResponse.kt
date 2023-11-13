package com.kuzmin.playlist.data.dto

import com.kuzmin.playlist.domain.model.TrackDto

data class TracksResponse(val resultCount: Int, val results: ArrayList<TrackDto>) : NetworkResponse()