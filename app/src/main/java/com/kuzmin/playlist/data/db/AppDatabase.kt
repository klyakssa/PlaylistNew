package com.kuzmin.playlist.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kuzmin.playlist.data.db.dao.PlaylistDao
import com.kuzmin.playlist.data.db.dao.TrackDao
import com.kuzmin.playlist.data.db.entity.PlaylistEntity
import com.kuzmin.playlist.data.db.entity.TrackEntity

@Database(version = 2, entities = [TrackEntity::class, PlaylistEntity::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}