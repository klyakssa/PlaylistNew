package com.kuzmin.playlist.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "playlist_id")
    val playlistId: Int,
    val playlistName: String,
    val playlistDescribe: String,
    val imgFilePath: String,
    val idAddedTracks: String,
    val countTracks: Int
)
