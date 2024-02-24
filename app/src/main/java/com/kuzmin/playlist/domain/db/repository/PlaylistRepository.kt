package com.kuzmin.playlist.domain.db.repository

import com.kuzmin.playlist.domain.model.IsTrackInPlaylist
import com.kuzmin.playlist.domain.model.PlaylistDto
import com.kuzmin.playlist.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun insertPlaylist(playlistName: String, playlistDescribe: String, imgFilePath: String): Flow<Resource<Error?>>

    suspend fun updateTracksInPlaylist(playlist: PlaylistDto, idTracks: String): Flow<IsTrackInPlaylist>

    fun getPlaylists(): Flow<List<PlaylistDto>>
}