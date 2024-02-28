package com.kuzmin.playlist.data.repository.PlaylistRepository

import android.content.Context
import android.widget.ArrayAdapter
import androidx.core.net.toUri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kuzmin.playlist.data.db.AppDatabase
import com.kuzmin.playlist.data.db.converters.PlaylistDbConverter
import com.kuzmin.playlist.data.db.converters.TrackDbConverter
import com.kuzmin.playlist.data.db.entity.PlaylistAndTracksEntity
import com.kuzmin.playlist.data.db.entity.PlaylistEntity
import com.kuzmin.playlist.data.db.entity.TrackEntity
import com.kuzmin.playlist.data.model.SaveFiles
import com.kuzmin.playlist.domain.db.repository.PlaylistRepository
import com.kuzmin.playlist.domain.model.IsTrackInPlaylist
import com.kuzmin.playlist.domain.model.PlaylistDto
import com.kuzmin.playlist.domain.model.Resource
import com.kuzmin.playlist.domain.model.TrackDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class PlaylistRepositoryImpl(
    private val context2: Context,
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val trackDbConverter: TrackDbConverter,
    private val saveFiles: SaveFiles,
) : PlaylistRepository {
    override suspend fun insertPlaylist(
        playlistName: String,
        playlistDescribe: String,
        imgFilePath: String,
        context: Context
    ): Flow<Resource<Error?>> = flow {
        if (!imgFilePath.isNullOrEmpty()) {
            val file = saveFiles.saveImageToPrivateStorage(playlistName, imgFilePath.toUri(), context)
            if(file == null){
                emit(Resource.Error("File is null"))
            }else{
                appDatabase.playlistDao().insertPlaylist(
                    playlistDbConverter.map(
                        playlistName,
                        playlistDescribe,
                        file.path
                    )
                )
                emit(Resource.Success(null))
            }
        }else{
            appDatabase.playlistDao().insertPlaylist(
                playlistDbConverter.map(
                    playlistName,
                    playlistDescribe,
                    ""
                )
            )
            emit(Resource.Success(null))
        }
    }

    override suspend fun updateTracksInPlaylist(
        playlist: PlaylistDto,
        trackDto: TrackDto
    ): Flow<IsTrackInPlaylist> = flow {
        val playlistAndTracks =
            appDatabase.playlistAndTracksDao().findTracksByPlaylist(playlist.playlistId)
        val track = playlistAndTracks.find { it.track.trackId == trackDto.trackId }
        if (track != null) {
            emit(IsTrackInPlaylist.isInPlaylist(playlist.playlistName))
        } else {
            appDatabase.playlistAndTracksDao().insert(
                PlaylistAndTracksEntity(
                    0,
                    convertFromTrackDto(trackDto),
                    playlist.playlistId.toLong()
                )
            )
            appDatabase.playlistDao().updateCountTracksInPlaylist(playlist.playlistId)
            emit(IsTrackInPlaylist.notInPlaylist(playlist.playlistName))
        }
    }

    override fun getPlaylists(): Flow<List<PlaylistDto>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<PlaylistDto> {
        return playlists.map { playlist -> playlistDbConverter.map(playlist, context2) }
    }

    private fun convertFromTrackDto(track: TrackDto): TrackEntity {
        return trackDbConverter.map(track)
    }

}