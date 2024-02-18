package com.kuzmin.playlist.domain.db.iterators

import com.kuzmin.playlist.domain.model.TrackDto
import kotlinx.coroutines.flow.Flow

interface FavoriteIterator {
    suspend fun insertTrack(track: TrackDto)

    suspend fun deleteTrack(track: TrackDto)

    fun getTracks(): Flow<List<TrackDto>>

    suspend fun existTrack(trackId: String): Flow<Boolean>
}