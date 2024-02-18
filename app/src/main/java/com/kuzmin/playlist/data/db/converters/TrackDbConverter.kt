package com.kuzmin.playlist.data.db.converters

import com.kuzmin.playlist.data.db.entity.TrackEntity
import com.kuzmin.playlist.domain.model.TrackDto
import java.text.SimpleDateFormat

class TrackDbConverter {
    fun map(track: TrackDto): TrackEntity {
        return TrackEntity(track.trackId, track.trackName, track.artistName, track.trackTime, track.artworkUrl100, track.collectionName, DateTimeMapper.formatDate(track.releaseDate), track.primaryGenreName, track.country, track.previewUrl)
    }

    fun map(track: TrackEntity): TrackDto {
        return TrackDto(track.trackId, track.trackName, track.artistName, track.trackTime, track.artworkUrl100, track.collectionName, DateTimeMapper.formatDate(track.releaseDate), track.primaryGenreName, track.country, track.previewUrl)
    }
}