package com.kuzmin.playlist.domain.searchTracksByName.consumer

interface Consumer<T> {
    fun consume(data: ConsumerData<T>)
}