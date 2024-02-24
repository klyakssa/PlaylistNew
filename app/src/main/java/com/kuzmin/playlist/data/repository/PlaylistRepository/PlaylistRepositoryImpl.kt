package com.kuzmin.playlist.data.repository.PlaylistRepository

import android.widget.ArrayAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kuzmin.playlist.data.db.AppDatabase
import com.kuzmin.playlist.data.db.converters.PlaylistDbConverter
import com.kuzmin.playlist.data.db.entity.PlaylistEntity
import com.kuzmin.playlist.domain.db.repository.PlaylistRepository
import com.kuzmin.playlist.domain.model.IsTrackInPlaylist
import com.kuzmin.playlist.domain.model.PlaylistDto
import com.kuzmin.playlist.domain.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val gson: Gson,
): PlaylistRepository {
    override suspend fun insertPlaylist(playlistName: String, playlistDescribe: String, imgFilePath: String): Flow<Resource<Error?>>  = flow {
        val id = appDatabase.playlistDao().insertPlaylist(playlistDbConverter.map(playlistName,playlistDescribe,imgFilePath))
        val playlistNew =  appDatabase.playlistDao().getPlaylistById(id.toInt())
        if (playlistNew == null) {
            emit(Resource.Error("Playlist is not added"))
        }
        emit(Resource.Success(null))
    }

    override suspend fun updateTracksInPlaylist(playlist: PlaylistDto, idTracks: String): Flow<IsTrackInPlaylist> = flow {
        val itemType = object : TypeToken<ArrayList<String>>() {}.type
        val tracks = appDatabase.playlistDao().getTracksFromPlaylist(playlist.playlistId)
        if (tracks.isNullOrEmpty()){
            val tracksN = ArrayList<String>()
            tracksN.add(idTracks)
            appDatabase.playlistDao().updateTracksInPlaylist(playlist.playlistId, gson.toJson(tracksN))
            emit(IsTrackInPlaylist.notInPlaylist(playlist.playlistName))
        }else{
            val json = gson.fromJson<ArrayList<String>>(tracks, itemType)
            if (json.contains(idTracks)){
                emit(IsTrackInPlaylist.isInPlaylist(playlist.playlistName))
            }else{
                json.add(idTracks)
                appDatabase.playlistDao().updateTracksInPlaylist(playlist.playlistId, gson.toJson(json))
                emit(IsTrackInPlaylist.notInPlaylist(playlist.playlistName))
            }
        }
    }

    override fun getPlaylists(): Flow<List<PlaylistDto>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<PlaylistDto> {
        return playlists.map { playlist -> playlistDbConverter.map(playlist) }
    }
}