package com.mdts.eieapp.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.MutableLiveData

class ConnectivityDataSource (private val applicationContext: Context) {
    private val connectivityManager = applicationContext
        .getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager


    private val _connectionChanged = MutableLiveData<Boolean>()

    val connected: Boolean
        get() {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager
                .getNetworkCapabilities(network)
                ?: return false
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
    init {
        registerNetworkChangesListener()
    }

    private fun registerNetworkChangesListener() {
        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    _connectionChanged.postValue(connected)
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    _connectionChanged.postValue(connected)
                }

                // lost network connection
                override fun onLost(network: Network) {
                    super.onLost(network)
                    _connectionChanged.postValue(connected)
                }
            }
        )
    }
}