package com.kuzmin.playlist.iTunesAPI

import retrofit2.Call
import retrofit2.http.*

interface itunesApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String) : Call<TracksResponse>
}