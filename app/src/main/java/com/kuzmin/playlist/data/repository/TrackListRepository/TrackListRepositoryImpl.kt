package com.kuzmin.playlist.data.repository.TrackListRepository

import android.content.Context
import com.kuzmin.playlist.R
import com.kuzmin.playlist.data.dto.TracksResponse
import com.kuzmin.playlist.domain.model.Resource
import com.kuzmin.playlist.data.model.TrackDto
import com.kuzmin.playlist.domain.searchTracksByName.repository.TracksListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackListRepositoryImpl(
    private val context: Context,
    private val tracksNetworkClient: TracksNetworkClient
): TracksListRepository {
    override fun getTracks(trackName: String?): Flow<Resource<ArrayList<TrackDto>>> = flow {
        val tracksResponse = tracksNetworkClient.getTracks(trackName)

        when (tracksResponse.resultCode) {
            -1 -> {
                emit(Resource.Error(context.getString(R.string.error)))
            }
            200 -> {
                with(tracksResponse as TracksResponse) {
                    val rates = tracksResponse.results
                    emit(Resource.Success(rates))
                }
            }
            else -> {
                emit(Resource.Error(context.getString(R.string.error)))
            }
        }
    }
}