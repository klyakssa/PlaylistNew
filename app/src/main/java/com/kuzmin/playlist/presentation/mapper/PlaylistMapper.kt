package com.kuzmin.playlist.presentation.mapper

import com.kuzmin.playlist.data.model.PlaylistDto
import com.kuzmin.playlist.presentation.models.Playlist

object PlaylistMapper {

    fun map(playlist: PlaylistDto): Playlist {
        return Playlist(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescribe,
            playlist.imgFilePath,
            playlist.idAddedTracks,
            playlist.countTracks
        )
    }

    fun map(playlist: Playlist): PlaylistDto {
        return PlaylistDto(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescribe,
            playlist.imgFilePath,
            playlist.idAddedTracks,
            playlist.countTracks
        )
    }
}