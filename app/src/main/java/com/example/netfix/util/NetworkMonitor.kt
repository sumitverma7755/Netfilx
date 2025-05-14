package com.example.netfix.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class NetworkMonitor(private val context: Context) {
    sealed class ConnectionState {
        object Available : ConnectionState()
        object Unavailable : ConnectionState()
    }

    val isOnline: Flow<ConnectionState> = callbackFlow {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                launch { send(ConnectionState.Available) }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                launch { send(ConnectionState.Unavailable) }
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, callback)

        // Set current state
        val currentState = if (connectivityManager.isNetworkAvailable()) {
            ConnectionState.Available
        } else {
            ConnectionState.Unavailable
        }
        launch { send(currentState) }

        // Remove callback when flow is closed
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()

    private fun ConnectivityManager.isNetworkAvailable(): Boolean {
        val network = activeNetwork ?: return false
        val capabilities = getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}