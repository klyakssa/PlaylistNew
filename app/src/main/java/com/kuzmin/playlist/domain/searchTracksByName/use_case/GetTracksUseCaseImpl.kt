package com.kuzmin.playlist.domain.searchTracksByName.use_case

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
        //return tracksRepository.getTracks(trackName)
        executor.execute {

            if (trackName == null) {
                consumer.consume(ConsumerData.Error("Что-то пошло не так, попробуйте еще раз :("))
            } else {
                when (val tracksResponse =
                    tracksRepository.getTracks(trackName)) {
                    is Resource.Success -> {

                       val tracks = tracksResponse.data
                        if(tracks.isEmpty()){
                            consumer.consume(ConsumerData.Error("Empty"))
                        }else{
                            consumer.consume(ConsumerData.Data(tracks))
                        }
                    }

                    is Resource.Error -> {
                        consumer.consume(ConsumerData.Error("Error"))
                    }
                }
            }
        }
    }
}