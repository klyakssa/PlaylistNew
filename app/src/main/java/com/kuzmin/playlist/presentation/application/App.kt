package com.kuzmin.playlist.presentation.application

import android.Manifest
import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.kuzmin.playlist.data.model.Preferences
import com.kuzmin.playlist.di.dataModule
import com.kuzmin.playlist.di.interactorModule
import com.kuzmin.playlist.di.repositoryModule
import com.kuzmin.playlist.di.useCaseModule
import com.kuzmin.playlist.di.viewModelModule
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {



    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule, useCaseModule)
        }
        switchTheme(get<SharedPreferences>().getBoolean(Preferences.DARK_THEME_KEY.pref, false))
        PermissionRequester.initialize(applicationContext)
        val requester = PermissionRequester.instance()
        runBlocking {
            launch {
                requester.request(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                ).collect { result ->
                    when (result) {
                        is PermissionResult.Denied -> return@collect
                        is PermissionResult.Denied.DeniedPermanently -> return@collect
                        is PermissionResult.Cancelled -> return@collect
                        is PermissionResult.Granted -> return@collect
                    }
                }
            }
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