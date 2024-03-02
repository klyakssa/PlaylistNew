package com.kuzmin.playlist.domain.db.iterators

import com.kuzmin.playlist.data.model.TrackDto
import kotlinx.coroutines.flow.Flow

interface FavoriteIterator {
    suspend fun insertTrack(track: TrackDto)

    suspend fun deleteTrack(track: TrackDto)

    fun getTracks(): Flow<List<TrackDto>>

    suspend fun existTrack(trackId: String): Flow<Boolean>

    fun initListenerOnUpdate(favoriteListener: FavoriteListener)

    interface FavoriteListener {
        fun callOnupdate()
    }
}