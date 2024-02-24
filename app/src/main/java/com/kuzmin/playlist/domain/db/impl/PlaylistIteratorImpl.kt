package com.kuzmin.playlist.domain.db.impl

import com.kuzmin.playlist.domain.db.iterators.PlaylistIterator
import com.kuzmin.playlist.domain.db.repository.PlaylistRepository
import com.kuzmin.playlist.domain.model.IsTrackInPlaylist
import com.kuzmin.playlist.domain.model.PlaylistDto
import com.kuzmin.playlist.domain.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistIteratorImpl(
    private val playlistRepository: PlaylistRepository
): PlaylistIterator {
    override suspend fun insertPlaylist(playlistName: String, playlistDescribe: String, imgFilePath: String): Flow<String?> {
        return playlistRepository.insertPlaylist(playlistName, playlistDescribe, imgFilePath).map { result ->
            when (result) {
                is Resource.Success -> {
                    null
                }
                is Resource.Error -> {
                    result.message
                }
            }
        }
    }

    override suspend fun updateTracksInPlaylist(playlist: PlaylistDto, idTracks: String): Flow<IsTrackInPlaylist> {
        return playlistRepository.updateTracksInPlaylist(playlist, idTracks)
    }

    override fun getPlaylists(): Flow<List<PlaylistDto>> {
        return playlistRepository.getPlaylists()
    }
}