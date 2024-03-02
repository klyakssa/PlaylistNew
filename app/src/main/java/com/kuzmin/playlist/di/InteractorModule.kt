package com.kuzmin.playlist.di

import com.kuzmin.playlist.domain.db.impl.FavoriteIteratorImpl
import com.kuzmin.playlist.domain.db.impl.PlaylistIteratorImpl
import com.kuzmin.playlist.domain.db.iterators.FavoriteIterator
import com.kuzmin.playlist.domain.db.iterators.PlaylistIterator
import com.kuzmin.playlist.domain.mediaplayer.impl.MediaPlayerInteractionImpl
import com.kuzmin.playlist.domain.mediaplayer.iterators.MediaPlayerIteractor
import com.kuzmin.playlist.domain.preferencesSearchHistory.impl.PreferencesSearchHistoryInteractionImpl
import com.kuzmin.playlist.domain.preferencesSearchHistory.iteractors.PreferencesSearchHistoryIteractor
import com.kuzmin.playlist.domain.preferencesTheme.impl.PreferencesThemeInteractionImpl
import com.kuzmin.playlist.domain.preferencesTheme.iterators.PreferencesThemeIteractor
import com.kuzmin.playlist.domain.sharing.impl.SharingInteractorImpl
import com.kuzmin.playlist.domain.sharing.iterators.SharingInteractor
import org.koin.dsl.module

val interactorModule = module {

    single<PreferencesSearchHistoryIteractor> {
        PreferencesSearchHistoryInteractionImpl(get())
    }

    factory<MediaPlayerIteractor> {
        MediaPlayerInteractionImpl(get())
    }

    single<PreferencesThemeIteractor> {
        PreferencesThemeInteractionImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<FavoriteIterator> {
        FavoriteIteratorImpl(get())
    }

    single<PlaylistIterator> {
        PlaylistIteratorImpl(get())
    }
}