package com.kuzmin.playlist.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.kuzmin.playlist.data.dto.NetworkResponse
import com.kuzmin.playlist.data.repository.TrackListRepository.TracksNetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TracksRetrofitNetworkClient(
    private val api: RetrofitApi,
    private val context: Context): TracksNetworkClient {
    override suspend fun getTracks(trackName: String?): NetworkResponse {
            if (!isConnected()) {
                return NetworkResponse().apply { resultCode = -1 }
            }

            if (trackName == null) {
                return NetworkResponse().apply { resultCode = -1 }
            }
        return withContext(Dispatchers.IO) {
            try {
                val response = api.search(trackName)
                response.apply { resultCode = 200 }
            } catch (ex: Exception) {
                NetworkResponse().apply { resultCode = 400 }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}