package com.kuzmin.playlist.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.kuzmin.playlist.data.dto.NetworkResponse
import com.kuzmin.playlist.data.repository.TrackListRepository.TracksNetworkClient

class TracksRetrofitNetworkClient(
    private val api: RetrofitApi,
    private val context: Context): TracksNetworkClient {
    override fun getTracks(trackName: String?): NetworkResponse {
        return try {
            if (!isConnected()) {
                return NetworkResponse().apply { resultCode = -1 }
            }
            val response = api.search(trackName).execute()
            val networkResponse = response.body() ?: NetworkResponse()

            networkResponse.apply { resultCode = response.code() }
        } catch (ex: Exception) {
            NetworkResponse().apply { resultCode = 400 }
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