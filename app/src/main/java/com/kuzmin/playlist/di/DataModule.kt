package com.kuzmin.playlist.di

import androidx.room.Room
import com.google.gson.GsonBuilder
import com.kuzmin.playlist.data.db.AppDatabase
import com.kuzmin.playlist.data.network.RetrofitApi
import com.kuzmin.playlist.data.network.TracksRetrofitNetworkClient
import com.kuzmin.playlist.data.repository.TrackListRepository.TracksNetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<RetrofitApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("YYYY-MM-dd'T'hh:mm:ss").create()))
            .build()
            .create(RetrofitApi::class.java)
    }

    single<TracksNetworkClient> {
        TracksRetrofitNetworkClient(get(), androidContext())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

}