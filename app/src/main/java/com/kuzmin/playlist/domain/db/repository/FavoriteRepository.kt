package com.kuzmin.playlist.domain.db.repository

import com.kuzmin.playlist.domain.model.TrackDto
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun insertTrack(trackDto: TrackDto)

    suspend fun deleteTrack(trackDto: TrackDto)

    fun getTracks(): Flow<List<TrackDto>>

    suspend fun existTrackById(trackId: String): Flow<Boolean>
}