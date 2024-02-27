package com.kuzmin.playlist.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kuzmin.playlist.data.db.dao.PlaylistAndTracksDao
import com.kuzmin.playlist.data.db.dao.PlaylistDao
import com.kuzmin.playlist.data.db.dao.TrackDao
import com.kuzmin.playlist.data.db.entity.PlaylistAndTracksEntity
import com.kuzmin.playlist.data.db.entity.PlaylistEntity
import com.kuzmin.playlist.data.db.entity.TrackEntity

@Database(version = 3, entities = [TrackEntity::class, PlaylistEntity::class, PlaylistAndTracksEntity::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistAndTracksDao(): PlaylistAndTracksDao
}