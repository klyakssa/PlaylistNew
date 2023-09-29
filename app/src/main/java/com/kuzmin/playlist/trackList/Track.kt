package com.kuzmin.playlist.trackList

import com.google.gson.annotations.SerializedName

data class Track(@SerializedName("trackName")val trackName: String, // Название композиции
                 @SerializedName("artistName")val artistName: String, // Имя исполнителя
                 @SerializedName("trackTimeMillis")val trackTime: String, // Продолжительность трека
                 @SerializedName("artworkUrl100")val artworkUrl100: String) // Ссылка на изображение обложки
