package com.kuzmin.playlist.di

import com.kuzmin.playlist.domain.searchTracksByName.api.GetTracksUseCase
import com.kuzmin.playlist.domain.searchTracksByName.use_case.GetTracksUseCaseImpl
import org.koin.dsl.module

val useCaseModule = module {


    single<GetTracksUseCase> {
        GetTracksUseCaseImpl(get())
    }

}