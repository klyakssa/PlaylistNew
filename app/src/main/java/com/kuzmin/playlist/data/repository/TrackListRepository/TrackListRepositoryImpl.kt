package com.kuzmin.playlist.data.repository.TrackListRepository

import android.content.Context
import com.kuzmin.playlist.R
import com.kuzmin.playlist.data.dto.TracksResponse
import com.kuzmin.playlist.domain.model.Resource
import com.kuzmin.playlist.domain.model.TrackDto
import com.kuzmin.playlist.domain.searchTracksByName.repository.TracksListRepository

class TrackListRepositoryImpl(
    private val context: Context,
    private val tracksNetworkClient: TracksNetworkClient
): TracksListRepository {
    override fun getTracks(trackName: String?): Resource<ArrayList<TrackDto>> {
        val tracksResponse = tracksNetworkClient.getTracks(trackName)

        return if (tracksResponse is TracksResponse) {
            val rates = tracksResponse.results
            Resource.Success(rates)
        } else {
            Resource.Error(context.getString(R.string.error))
        }
    }
}