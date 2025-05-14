package com.example.netfix.util

import com.example.netfix.api.LMStudioApi
import com.example.netfix.repository.LMStudioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

object LMStudioConnection {
    private const val DEFAULT_PORT = 1234
    private const val CONNECTION_TIMEOUT = 2000 // 2 seconds

    /**
     * Tests if LM Studio server is reachable at the given IP address
     * @param ipAddress The IP address of the computer running LM Studio (e.g. "192.168.1.100")
     * @return true if connection successful, false otherwise
     */
    suspend fun testConnection(ipAddress: String): Boolean = withContext(Dispatchers.IO) {
        try {
            Socket().use { socket ->
                socket.connect(InetSocketAddress(ipAddress, DEFAULT_PORT), CONNECTION_TIMEOUT)
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Configures LM Studio API with the provided IP address
     * @param ipAddress The IP address of the computer running LM Studio
     */
    fun configure(ipAddress: String) {
        val baseUrl = "http://$ipAddress:$DEFAULT_PORT/"
        LMStudioApi.setBaseUrl(baseUrl)
    }
}
