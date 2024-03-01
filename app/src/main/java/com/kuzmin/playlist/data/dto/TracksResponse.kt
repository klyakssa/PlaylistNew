package com.kuzmin.playlist.data.dto

import com.kuzmin.playlist.data.model.TrackDto

data class TracksResponse(val resultCount: Int, val results: ArrayList<TrackDto>) : NetworkResponse()