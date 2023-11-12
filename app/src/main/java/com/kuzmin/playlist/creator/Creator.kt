package com.kuzmin.playlist.creator

import android.app.Application
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.kuzmin.playlist.data.repository.MediaPlayerRepositoryImpl
import com.kuzmin.playlist.data.repository.PreferencesRepositoryImpl
import com.kuzmin.playlist.domain.repository.MediaPlayerRepository
import com.kuzmin.playlist.domain.repository.PreferencesRepository
import com.kuzmin.playlist.domain.use_case.WorkWithMediaPlayerUseCase
import com.kuzmin.playlist.domain.use_case.WorkWithPreferencesUseCase

object Creator {
    lateinit var workWithPreferencesUseCase: WorkWithPreferencesUseCase

    fun initPreferences(sp: SharedPreferences){
       this.workWithPreferencesUseCase = WorkWithPreferencesUseCase(providePreferencesRepository(sp))
    }

    private fun providePreferencesRepository(sp: SharedPreferences): PreferencesRepository {
        return PreferencesRepositoryImpl(sp)
    }

    fun provideWorkWithMediaPlayerUseCase(): WorkWithMediaPlayerUseCase{
        return WorkWithMediaPlayerUseCase(provideMediaPlayerRepository())
    }

    private fun provideMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }
}