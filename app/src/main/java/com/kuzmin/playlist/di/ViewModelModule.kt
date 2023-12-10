package com.kuzmin.playlist.di

import com.kuzmin.playlist.presentation.audioplayer.view_model.PlayerViewModel
import com.kuzmin.playlist.presentation.search.view_model.TracksSearchViewModel
import com.kuzmin.playlist.presentation.settings.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel { (url: String) ->
        PlayerViewModel(url,get())
    }

    viewModel {
        TracksSearchViewModel(androidContext(),get(),get())
    }

    viewModel {
        SettingsViewModel(androidContext(),get(),get())
    }
}