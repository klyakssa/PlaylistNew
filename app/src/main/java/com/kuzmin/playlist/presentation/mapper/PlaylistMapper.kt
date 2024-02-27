package com.kuzmin.playlist.presentation.mapper

import android.content.Context
import android.os.Environment
import com.kuzmin.playlist.domain.model.PlaylistDto
import com.kuzmin.playlist.presentation.models.Playlist
import java.io.File

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