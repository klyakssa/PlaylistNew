package com.kuzmin.playlist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kuzmin.playlist.data.db.entity.PlaylistAndTracksEntity
import com.kuzmin.playlist.data.db.entity.TrackEntity


@Dao
interface PlaylistAndTracksDao {
    @Insert
    fun insert(pAndTrackEntity: PlaylistAndTracksEntity)

    @Query("SELECT * FROM playlist_and_tracks_table WHERE playlistId=:playlistId")
    fun findTracksByPlaylist(playlistId: Int): List<PlaylistAndTracksEntity>
}