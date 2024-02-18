package com.kuzmin.playlist.data.repository.FavoriteRepository

import com.kuzmin.playlist.data.db.AppDatabase
import com.kuzmin.playlist.data.db.converters.TrackDbConverter
import com.kuzmin.playlist.data.db.entity.TrackEntity
import com.kuzmin.playlist.domain.db.repository.FavoriteRepository
import com.kuzmin.playlist.domain.model.TrackDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val movieDbConvertor: TrackDbConverter,
) : FavoriteRepository {
    override suspend fun insertTrack(trackDto: TrackDto) {
        withContext(Dispatchers.IO) {
            appDatabase.trackDao().insertTrackFav(track = convertFromTrackDto(trackDto))
        }
    }

    override suspend fun deleteTrack(trackDto: TrackDto) {
        withContext(Dispatchers.IO) {
            appDatabase.trackDao().deleteTrackFav(track = convertFromTrackDto(trackDto))
        }
    }

    override fun getTracks(): Flow<List<TrackDto>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun existTrackById(trackId: String): Flow<Boolean> = flow {
        emit(appDatabase.trackDao().existTrackById(trackId) != null)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<TrackDto> {
        return tracks.map { movie -> movieDbConvertor.map(movie) }
    }

    private fun convertFromTrackDto(track: TrackDto): TrackEntity {
        return movieDbConvertor.map(track)
    }
}