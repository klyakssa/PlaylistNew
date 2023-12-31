package com.kuzmin.playlist.domain.searchTracksByName.use_case

import com.kuzmin.playlist.R
import com.kuzmin.playlist.domain.model.Resource
import com.kuzmin.playlist.domain.model.TrackDto
import com.kuzmin.playlist.domain.searchTracksByName.api.GetTracksUseCase
import com.kuzmin.playlist.domain.searchTracksByName.consumer.Consumer
import com.kuzmin.playlist.domain.searchTracksByName.consumer.ConsumerData
import com.kuzmin.playlist.domain.searchTracksByName.repository.TracksListRepository
import java.util.concurrent.Executors


class GetTracksUseCaseImpl(
    private val tracksRepository: TracksListRepository
): GetTracksUseCase {

    private val executor = Executors.newCachedThreadPool()
    override fun execute(
        trackName: String?,
        consumer: Consumer<ArrayList<TrackDto>>
    ) {
        executor.execute {
            when (val tracksResponse = tracksRepository.getTracks(trackName)) {
                is Resource.Success -> { consumer.consume(tracksResponse.data, null) }
                is Resource.Error -> { consumer.consume(null, tracksResponse.message) }
            }
        }
    }
}