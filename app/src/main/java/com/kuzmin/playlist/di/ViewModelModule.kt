package com.kuzmin.playlist.di

import androidx.fragment.app.FragmentActivity
import com.kuzmin.playlist.presentation.audioplayer.view_model.PlayerViewModel
import com.kuzmin.playlist.presentation.library.Fragments.Favorite.view_models.FavoriteViewModel
import com.kuzmin.playlist.presentation.library.Fragments.Playlist.AddNewPlaylist.CreatePlaylist
import com.kuzmin.playlist.presentation.library.Fragments.Playlist.AddNewPlaylist.view_models.CreatePlaylistViewModel
import com.kuzmin.playlist.presentation.library.Fragments.Playlist.view_models.PlaylistViewModel
import com.kuzmin.playlist.presentation.search.view_model.TracksSearchViewModel
import com.kuzmin.playlist.presentation.settings.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel {
        FavoriteViewModel(get())
    }

    viewModel { (url: String) ->
        PlayerViewModel(url,get(), get(), get())
    }

    viewModel {
        TracksSearchViewModel(androidContext(),get(),get())
    }

    viewModel {
        SettingsViewModel(androidContext(),get(),get())
    }

    viewModel {
        PlaylistViewModel(get())
    }

    viewModel {
        CreatePlaylistViewModel(get())
    }
}