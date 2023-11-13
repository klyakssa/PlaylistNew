package com.kuzmin.playlist.data.network

import com.kuzmin.playlist.data.dto.NetworkResponse
import com.kuzmin.playlist.data.repository.TrackListRepository.TracksNetworkClient

class TracksRetrofitNetworkClient: TracksNetworkClient {
    override fun getTracks(trackName: String?): NetworkResponse {
        return try {
            val response = RetrofitClient.api.search(trackName).execute()
            val networkResponse = response.body() ?: NetworkResponse()

            networkResponse.apply { resultCode = response.code() }
        } catch (ex: Exception) {
            NetworkResponse().apply { resultCode = 400 }
        }
    }
}