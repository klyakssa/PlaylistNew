package com.kuzmin.playlist.data.repository.TrackListRepository

import com.kuzmin.playlist.data.dto.NetworkResponse

interface TracksNetworkClient {
    fun getTracks(trackName: String?): NetworkResponse
}