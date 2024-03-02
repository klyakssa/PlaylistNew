package com.kuzmin.playlist.data.db.converters

import android.content.Context
import android.os.Environment
import com.kuzmin.playlist.R
import com.kuzmin.playlist.data.db.entity.PlaylistEntity
import com.kuzmin.playlist.data.model.PlaylistDto
import java.io.File

class PlaylistDbConverter {
    fun map(playlist: PlaylistDto): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescribe,
            playlist.imgFilePath.path,
            playlist.idAddedTracks,
            playlist.countTracks
        )
    }

    fun map(playlist: PlaylistEntity, context: Context): PlaylistDto {
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), context.getString(
                R.string.PlaylistMaker
            )
        )
        val file = File(filePath, "${playlist.playlistName}.jpg")
        return PlaylistDto(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescribe,
            file,
            playlist.idAddedTracks,
            playlist.countTracks
        )
    }

    fun map(playlistName: String, playlistDescribe: String, imgFilePath: String): PlaylistEntity {
        return PlaylistEntity(0, playlistName, playlistDescribe, imgFilePath, "", 0)
    }
}