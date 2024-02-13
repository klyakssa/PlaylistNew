package com.kuzmin.playlist.domain.searchTracksByName.consumer

interface Consumer<T> {
    fun consume(data: T?, errorMessage: String?)
}