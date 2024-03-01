package com.kuzmin.playlist.domain.db.impl

import com.kuzmin.playlist.domain.db.iterators.FavoriteIterator
import com.kuzmin.playlist.domain.db.repository.FavoriteRepository
import com.kuzmin.playlist.data.model.TrackDto
import kotlinx.coroutines.flow.Flow

class FavoriteIteratorImpl(
    private val favoriteRepository: FavoriteRepository
): FavoriteIterator {

    private lateinit var listener: FavoriteIterator.FavoriteListener

    override suspend fun insertTrack(track: TrackDto) {
        favoriteRepository.insertTrack(track)
        listener.callOnupdate()
    }

    override suspend fun deleteTrack(track: TrackDto) {
        favoriteRepository.deleteTrack(track)
    }

    override fun getTracks(): Flow<List<TrackDto>> {
        return favoriteRepository.getTracks()
    }

    override suspend fun existTrack(trackId: String): Flow<Boolean>  {
        return favoriteRepository.existTrackById(trackId)
    }

    override fun initListenerOnUpdate(favoriteListener: FavoriteIterator.FavoriteListener) {
        this.listener = favoriteListener
    }
}