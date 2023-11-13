package com.kuzmin.playlist.creator

import android.content.SharedPreferences
import com.kuzmin.playlist.data.network.TracksRetrofitNetworkClient
import com.kuzmin.playlist.data.repository.MediaPlayer.MediaPlayerRepositoryImpl
import com.kuzmin.playlist.data.repository.PreferencesSearchHistory.PreferencesSearchHistoryRepositoryImpl
import com.kuzmin.playlist.data.repository.PreferencesTheme.PreferencesThemeRepositoryImpl
import com.kuzmin.playlist.data.repository.TrackListRepository.TrackListRepositoryImpl
import com.kuzmin.playlist.data.repository.TrackListRepository.TracksNetworkClient
import com.kuzmin.playlist.domain.mediaplayer.iterators.MediaPlayerIteractor
import com.kuzmin.playlist.domain.mediaplayer.repository.MediaPlayerRepository
import com.kuzmin.playlist.domain.mediaplayer.impl.MediaPlayerInteractionImpl
import com.kuzmin.playlist.domain.preferencesSearchHistory.impl.PreferencesSearchHistoryInteractionImpl
import com.kuzmin.playlist.domain.preferencesSearchHistory.iteractors.PreferencesSearchHistoryIteractor
import com.kuzmin.playlist.domain.preferencesSearchHistory.repository.PreferencesSearchHistoryRepository
import com.kuzmin.playlist.domain.preferencesTheme.impl.PreferencesThemeInteractionImpl
import com.kuzmin.playlist.domain.preferencesTheme.iterators.PreferencesThemeIteractor
import com.kuzmin.playlist.domain.preferencesTheme.repository.PreferencesThemeRepository
import com.kuzmin.playlist.domain.searchTracksByName.api.GetTracksUseCase
import com.kuzmin.playlist.domain.searchTracksByName.repository.TracksListRepository
import com.kuzmin.playlist.domain.searchTracksByName.use_case.GetTracksUseCaseImpl

object Creator {
    private fun getMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }
    fun provideMediaPlayerInteraction(): MediaPlayerIteractor {
        return MediaPlayerInteractionImpl(getMediaPlayerRepository())
    }
    private fun getPreferencesThemeRepository(sp: SharedPreferences): PreferencesThemeRepository {
        return PreferencesThemeRepositoryImpl(sp)
    }
    fun providePreferencesThemeInteraction(sp: SharedPreferences): PreferencesThemeIteractor {
        return PreferencesThemeInteractionImpl(getPreferencesThemeRepository(sp))
    }

    private fun getPreferencesSearchHistoryRepository(sp: SharedPreferences): PreferencesSearchHistoryRepository {
        return PreferencesSearchHistoryRepositoryImpl(sp)
    }
    fun providePreferencesSearchHistoryInteraction(sp: SharedPreferences): PreferencesSearchHistoryIteractor {
        return PreferencesSearchHistoryInteractionImpl(getPreferencesSearchHistoryRepository(sp))
    }
    private fun getTracksNetworkClient(): TracksNetworkClient {
        return TracksRetrofitNetworkClient()
    }
    private fun getTrackListRepository(): TracksListRepository {
        return TrackListRepositoryImpl(getTracksNetworkClient())
    }
    fun provideGetTracksListUseCase(): GetTracksUseCase {
        return GetTracksUseCaseImpl(getTrackListRepository())
    }
}