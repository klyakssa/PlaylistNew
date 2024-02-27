package com.kuzmin.playlist.domain.db.iterators


import com.kuzmin.playlist.domain.model.IsTrackInPlaylist
import com.kuzmin.playlist.domain.model.PlaylistDto
import com.kuzmin.playlist.domain.model.TrackDto
import kotlinx.coroutines.flow.Flow

interface PlaylistIterator {
    suspend fun insertPlaylist(playlistName: String, playlistDescribe: String, imgFilePath: String): Flow<String?>

    suspend fun updateTracksInPlaylist(playlist: PlaylistDto, trackDto: TrackDto): Flow<IsTrackInPlaylist>

    fun getPlaylists(): Flow<List<PlaylistDto>>

    fun initListenerOnUpdate(playlistListener: PlaylistListener)

    interface PlaylistListener {
        fun callOnupdate()
    }
}