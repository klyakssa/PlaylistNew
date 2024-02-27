package com.kuzmin.playlist.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlist_and_tracks_table",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = arrayOf("playlist_id"),
            childColumns = arrayOf("playlistId"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class PlaylistAndTracksEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @Embedded
    val track: TrackEntity,

    val playlistId: Long,
)