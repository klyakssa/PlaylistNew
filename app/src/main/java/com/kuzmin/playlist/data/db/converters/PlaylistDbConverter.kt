package com.kuzmin.playlist.data.db.converters

import com.kuzmin.playlist.data.db.entity.PlaylistEntity
import com.kuzmin.playlist.domain.model.PlaylistDto

class PlaylistDbConverter {
    fun map(playlist: PlaylistDto): PlaylistEntity {
        return PlaylistEntity(playlist.playlistId,playlist.playlistName,playlist.playlistDescribe,playlist.imgFilePath,playlist.idAddedTracks,playlist.countTracks)
    }

    fun map(playlist: PlaylistEntity): PlaylistDto {
        return PlaylistDto(playlist.playlistId,playlist.playlistName,playlist.playlistDescribe,playlist.imgFilePath,playlist.idAddedTracks,playlist.countTracks)
    }
    fun map(playlistName: String, playlistDescribe: String, imgFilePath: String): PlaylistEntity {
        return PlaylistEntity(0,playlistName,playlistDescribe,imgFilePath,"",0)
    }
}