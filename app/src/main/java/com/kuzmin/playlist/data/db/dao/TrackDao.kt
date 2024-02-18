package com.kuzmin.playlist.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuzmin.playlist.data.db.entity.TrackEntity
import com.kuzmin.playlist.domain.model.TrackDto


@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackFav(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrackFav(track: TrackEntity)

    @Query("SELECT * FROM tracks_fav_table")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT * FROM tracks_fav_table WHERE track_id = :trackId")
    suspend fun existTrackById(trackId: String): TrackEntity
}