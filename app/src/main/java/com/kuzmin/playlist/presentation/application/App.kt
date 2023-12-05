package com.kuzmin.playlist.presentation.application

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.kuzmin.playlist.creator.Creator
import com.kuzmin.playlist.data.repository.share.ExternalNavigatorImpl
import com.kuzmin.playlist.di.dataModule
import com.kuzmin.playlist.di.interactorModule
import com.kuzmin.playlist.di.repositoryModule
import com.kuzmin.playlist.di.useCaseModule
import com.kuzmin.playlist.di.viewModelModule
import com.kuzmin.playlist.domain.sharing.impl.SharingInteractorImpl
import com.kuzmin.playlist.domain.sharing.iterators.SharingInteractor
import com.kuzmin.playlist.domain.sharing.repository.ExternalNavigator
import com.kuzmin.playlist.presentation.settings.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule, useCaseModule)
        }
        val currentNightMode = baseContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            switchTheme(true)
        }else{
            switchTheme(false)
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean){
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}