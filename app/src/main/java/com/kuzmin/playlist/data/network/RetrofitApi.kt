package com.kuzmin.playlist.data.network

import com.kuzmin.playlist.data.dto.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String?) : Call<TracksResponse>
}