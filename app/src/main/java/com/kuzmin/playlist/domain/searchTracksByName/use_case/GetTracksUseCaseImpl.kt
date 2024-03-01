package com.kuzmin.playlist.domain.searchTracksByName.use_case

import com.kuzmin.playlist.domain.model.Resource
import com.kuzmin.playlist.data.model.TrackDto
import com.kuzmin.playlist.domain.searchTracksByName.api.GetTracksUseCase
import com.kuzmin.playlist.domain.searchTracksByName.repository.TracksListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class GetTracksUseCaseImpl(
    private val tracksRepository: TracksListRepository
): GetTracksUseCase {

    override fun execute(trackName: String?): Flow<Pair<ArrayList<TrackDto>?, String?>> {
        return tracksRepository.getTracks(trackName).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}