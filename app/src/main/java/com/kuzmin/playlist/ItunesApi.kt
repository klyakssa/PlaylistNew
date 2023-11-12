package com.kuzmin.playlist

import retrofit2.Call
import retrofit2.http.*
import kotlin.String

interface itunesApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String) : Call<TracksResponse>
}