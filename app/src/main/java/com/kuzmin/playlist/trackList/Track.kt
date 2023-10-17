package com.kuzmin.playlist.trackList

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Track(@SerializedName("trackId")val trackId: String, // Название композиции
                 @SerializedName("trackName")val trackName: String, // Название композиции
                 @SerializedName("artistName")val artistName: String, // Имя исполнителя
                 @SerializedName("trackTimeMillis")val trackTime: String, // Продолжительность трека
                 @SerializedName("artworkUrl100")val artworkUrl100: String,
                 @SerializedName("collectionName")val collectionName: String?,
                 @SerializedName("releaseDate")val releaseDate: Date,
                 @SerializedName("primaryGenreName")val primaryGenreName: String,
                 @SerializedName("country")val country: String,
                 @SerializedName("previewUrl") val previewUrl: String
)
{
     fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
 }
