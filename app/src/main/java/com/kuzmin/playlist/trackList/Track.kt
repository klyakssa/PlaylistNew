package com.kuzmin.playlist.trackList

data class Track(val trackName: String, // Название композиции
                 val artistName: String, // Имя исполнителя
                 val trackTime: String, // Продолжительность трека
                 val artworkUrl100: String) // Ссылка на изображение обложки
