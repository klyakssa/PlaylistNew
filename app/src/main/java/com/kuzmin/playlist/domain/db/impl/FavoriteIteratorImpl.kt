package com.kuzmin.playlist.domain.db.impl

import com.kuzmin.playlist.domain.db.iterators.FavoriteIterator
import com.kuzmin.playlist.domain.db.repository.FavoriteRepository
import com.kuzmin.playlist.domain.model.TrackDto
import kotlinx.coroutines.flow.Flow

class FavoriteIteratorImpl(
    private val favoriteRepository: FavoriteRepository
): FavoriteIterator {
    override suspend fun insertTrack(track: TrackDto) {
        favoriteRepository.insertTrack(track)
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
}