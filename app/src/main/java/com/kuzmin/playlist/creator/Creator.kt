package com.kuzmin.playlist.creator

import android.content.SharedPreferences
import com.kuzmin.playlist.data.repository.MediaPlayerRepositoryImpl
import com.kuzmin.playlist.data.repository.PreferencesRepositoryImpl
import com.kuzmin.playlist.domain.iterators.mediaplayer.MediaPlayerIteractor
import com.kuzmin.playlist.domain.repository.mediaplayer.MediaPlayerRepository
import com.kuzmin.playlist.domain.impl.mediaplayer.MediaPlayerInteractionImpl
import com.kuzmin.playlist.domain.impl.preferences.PreferencesInteractionImpl
import com.kuzmin.playlist.domain.iterators.preferences.PreferencesIteractor
import com.kuzmin.playlist.domain.repository.preferences.PreferencesRepository

object Creator {
    private fun getMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }
    fun provideMediaPlayerInteraction(): MediaPlayerIteractor {
        return MediaPlayerInteractionImpl(getMediaPlayerRepository())
    }
    private fun getPreferencesRepository(sp: SharedPreferences): PreferencesRepository {
        return PreferencesRepositoryImpl(sp)
    }
    fun providePreferencesInteraction(sp: SharedPreferences): PreferencesIteractor {
        return PreferencesInteractionImpl(getPreferencesRepository(sp))
    }


}