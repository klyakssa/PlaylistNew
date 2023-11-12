package com.kuzmin.playlist

import com.kuzmin.playlist.domain.model.TrackDto

class TracksResponse(val resultCount: Int, val results: ArrayList<TrackDto>)  {
}