package com.kuzmin.playlist.di

import androidx.fragment.app.FragmentActivity
import com.kuzmin.playlist.presentation.audioplayer.model.UpdateLibrary
import com.kuzmin.playlist.presentation.audioplayer.view_model.PlayerViewModel
import com.kuzmin.playlist.presentation.library.Fragments.Favorite.view_models.FavoriteViewModel
import com.kuzmin.playlist.presentation.library.Fragments.Playlist.AddNewPlaylist.CreatePlaylist
import com.kuzmin.playlist.presentation.library.Fragments.Playlist.AddNewPlaylist.view_models.CreatePlaylistViewModel
import com.kuzmin.playlist.presentation.library.Fragments.Playlist.view_models.PlaylistViewModel
import com.kuzmin.playlist.presentation.library.Fragments.model.UpdatePlaylist
import com.kuzmin.playlist.presentation.search.view_model.TracksSearchViewModel
import com.kuzmin.playlist.presentation.settings.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    single<UpdateLibrary> {
        FavoriteViewModel(get())
    }

    single<UpdatePlaylist> {
        PlaylistViewModel(get())
    }

    viewModel {
        FavoriteViewModel(get())
    }

    viewModel { (url: String) ->
        PlayerViewModel(url,get(), get(), get(), get())
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

    viewModel {(activity: FragmentActivity) ->
        CreatePlaylistViewModel(activity, get(), get())
    }
}