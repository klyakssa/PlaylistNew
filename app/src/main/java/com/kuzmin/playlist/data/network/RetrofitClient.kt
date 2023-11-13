package com.kuzmin.playlist.data.network

import com.google.gson.GsonBuilder
import com.kuzmin.playlist.itunesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val client: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("YYYY-MM-dd'T'hh:mm:ss").create()))
            .build()
    }

    val api: RetrofitApi by lazy {
        client.create(RetrofitApi::class.java)
    }
}
