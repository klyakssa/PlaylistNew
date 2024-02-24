package com.kuzmin.playlist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuzmin.playlist.data.db.entity.PlaylistEntity
import com.kuzmin.playlist.data.db.entity.TrackEntity

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Query("SELECT idAddedTracks FROM playlist_table WHERE playlist_id =:idPlaylist")
    suspend fun getTracksFromPlaylist(idPlaylist: Int): String

    @Query("UPDATE playlist_table SET idAddedTracks = :idTracks WHERE playlist_id = :idPlaylist")
    suspend fun updateTracksInPlaylist(idPlaylist: Int, idTracks: String)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table WHERE playlist_id = :idPlaylist")
    suspend fun getPlaylistById(idPlaylist: Int): PlaylistEntity

}