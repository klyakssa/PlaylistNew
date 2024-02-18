package com.kuzmin.playlist.di

import com.kuzmin.playlist.presentation.audioplayer.view_model.PlayerViewModel
import com.kuzmin.playlist.presentation.library.Fragments.Favorite.view_models.FavoriteViewModel
import com.kuzmin.playlist.presentation.library.Fragments.Playlist.view_models.PlaylistViewModel
import com.kuzmin.playlist.presentation.search.view_model.TracksSearchViewModel
import com.kuzmin.playlist.presentation.settings.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel { (url: String) ->
        PlayerViewModel(url,get(), get())
    }

    viewModel {
        TracksSearchViewModel(androidContext(),get(),get())
    }

    viewModel {
        SettingsViewModel(androidContext(),get(),get())
    }

    viewModel {
        FavoriteViewModel(get())
    }

    viewModel {
        PlaylistViewModel()
    }
}