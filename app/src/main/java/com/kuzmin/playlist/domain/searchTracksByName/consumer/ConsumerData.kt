package com.kuzmin.playlist.domain.searchTracksByName.consumer

sealed interface ConsumerData<T> {
    data class Data<T>(val value: T) : ConsumerData<T>
    data class Error<T>(val message: String) : ConsumerData<T>
}