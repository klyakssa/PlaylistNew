package com.kuzmin.playlist.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.Companion.LOCALIZED
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tracks_fav_table")
data class TrackEntity(
    @PrimaryKey @ColumnInfo(name = "track_id")
    val trackId: String,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTime: String, // Продолжительность трека
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
    )