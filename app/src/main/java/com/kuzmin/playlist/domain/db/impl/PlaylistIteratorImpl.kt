package com.kuzmin.playlist.domain.db.impl

import android.content.Context
import com.kuzmin.playlist.domain.db.iterators.PlaylistIterator
import com.kuzmin.playlist.domain.db.repository.PlaylistRepository
import com.kuzmin.playlist.domain.model.IsTrackInPlaylist
import com.kuzmin.playlist.domain.model.PlaylistDto
import com.kuzmin.playlist.domain.model.Resource
import com.kuzmin.playlist.domain.model.TrackDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistIteratorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistIterator {

    private val listener: ArrayList<PlaylistIterator.PlaylistListener> = ArrayList()
    override suspend fun insertPlaylist(
        playlistName: String,
        playlistDescribe: String,
        imgFilePath: String,
        context: Context
    ): Flow<String?> {
        return playlistRepository.insertPlaylist(
            playlistName,
            playlistDescribe,
            imgFilePath,
            context
        )
            .map { result ->
                when (result) {
                    is Resource.Success -> {
                        null
                    }

                    is Resource.Error -> {
                        result.message
                    }
                }
            }
            .also {result ->
                when (result) {
                    null -> {
                        listener.forEach {
                            it.callOnupdate()
                        }
                    }
                }
            }
    }

    override suspend fun updateTracksInPlaylist(
        playlist: PlaylistDto,
        trackDto: TrackDto
    ): Flow<IsTrackInPlaylist> {
        val result = playlistRepository.updateTracksInPlaylist(playlist, trackDto)
        listener.forEach {
            it.callOnupdate()
        }
        return result
    }

    override fun getPlaylists(): Flow<List<PlaylistDto>> {
        return playlistRepository.getPlaylists()
    }

    override fun initListenerOnUpdate(playlistListener: PlaylistIterator.PlaylistListener) {
        val listener = this.listener.find {
            it == playlistListener
        }
        if (listener == null) {
            this.listener.add(playlistListener)
        }
    }
}