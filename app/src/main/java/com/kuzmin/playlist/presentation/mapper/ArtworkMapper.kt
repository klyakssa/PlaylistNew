package com.kuzmin.playlist.presentation.mapper

object ArtworkMapper {
    fun getCoverArtwork(artworkUrl100: String) = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
}