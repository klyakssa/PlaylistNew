package com.kuzmin.playlist.di

import android.app.Application
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.google.gson.Gson
import com.kuzmin.playlist.data.db.converters.PlaylistDbConverter
import com.kuzmin.playlist.data.db.converters.TrackDbConverter
import com.kuzmin.playlist.data.repository.FavoriteRepository.FavoriteRepositoryImpl
import com.kuzmin.playlist.data.repository.MediaPlayer.MediaPlayerRepositoryImpl
import com.kuzmin.playlist.data.repository.PlaylistRepository.PlaylistRepositoryImpl
import com.kuzmin.playlist.data.repository.PreferencesSearchHistory.PreferencesSearchHistoryRepositoryImpl
import com.kuzmin.playlist.data.repository.PreferencesTheme.PreferencesThemeRepositoryImpl
import com.kuzmin.playlist.data.repository.TrackListRepository.TrackListRepositoryImpl
import com.kuzmin.playlist.data.repository.share.ExternalNavigatorImpl
import com.kuzmin.playlist.domain.db.repository.FavoriteRepository
import com.kuzmin.playlist.domain.db.repository.PlaylistRepository
import com.kuzmin.playlist.domain.mediaplayer.repository.MediaPlayerRepository
import com.kuzmin.playlist.domain.preferencesSearchHistory.repository.PreferencesSearchHistoryRepository
import com.kuzmin.playlist.domain.preferencesTheme.repository.PreferencesThemeRepository
import com.kuzmin.playlist.domain.searchTracksByName.repository.TracksListRepository
import com.kuzmin.playlist.domain.sharing.repository.ExternalNavigator
import com.kuzmin.playlist.presentation.library.Fragments.Playlist.view_models.PlaylistViewModel
import com.kuzmin.playlist.presentation.library.Fragments.model.UpdatePlaylist
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<TracksListRepository> {
        TrackListRepositoryImpl(androidContext(),get())
    }

    factory {
        Gson()
    }

    factory { TrackDbConverter() }

    factory { PlaylistDbConverter() }

    single<SharedPreferences>{
        androidContext()
            .getSharedPreferences("playlist_preferences",
                Application.MODE_PRIVATE
            )
    }

    single<PreferencesSearchHistoryRepository> {
        PreferencesSearchHistoryRepositoryImpl(get(), get())
    }

    factory {
        MediaPlayer()
    }

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    single<PreferencesThemeRepository> {
        PreferencesThemeRepositoryImpl(get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl()
    }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get())
    }
}