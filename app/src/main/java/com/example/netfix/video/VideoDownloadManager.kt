package com.example.netfix.video

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.offline.VideoDownloadService
import androidx.media3.common.util.UnstableApi
import com.example.netfix.data.DownloadPreferences
import com.example.netfix.model.Movie
import com.example.netfix.util.NetworkMonitor
import com.example.netfix.util.StorageHelper
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

val Context.dataStore by preferencesDataStore(name = "downloads")

@UnstableApi
class VideoDownloadManager(private val context: Context) {
    val autoDeleteEnabled: Flow<Boolean> = downloadPreferences.autoDeleteWatched

    private val DOWNLOADED_MOVIES = stringSetPreferencesKey("downloaded_movies")
    private val downloadPreferences = DownloadPreferences(context)
    private val networkMonitor = NetworkMonitor(context)
    private val progressMonitor = DownloadProgressMonitor.getInstance()
    private val storageHelper = StorageHelper(context)
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val downloadManager = VideoDownloadService.getDownloadService(context)
    private val _downloads = MutableStateFlow<Map<String, Pair<Float, String>>>(emptyMap())
    val downloads: StateFlow<Map<String, Pair<Float, String>>> = _downloads.asStateFlow()

    private val _downloadState = MutableStateFlow<DownloadState>(DownloadState.Idle)
    val downloadState = _downloadState.asStateFlow()

    val downloadProgress: StateFlow<Map<Int, Float>> = progressMonitor.downloadProgress

    enum class DownloadStatus {
        NOT_DOWNLOADED,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        QUEUED,
        REMOVING,
        PAUSED
    }

    init {
        coroutineScope.launch {
            networkMonitor.isOnline.collect { state ->
                when (state) {
                    is NetworkMonitor.ConnectionState.Available -> {
                        resumePendingDownloads()
                    }
                    is NetworkMonitor.ConnectionState.Unavailable -> {
                        _downloadState.value = DownloadState.NoNetwork
                    }
                }
            }
        }
    }

    suspend fun downloadMovie(movie: Movie) {
        // Check storage space
        if (!storageHelper.hasEnoughSpaceForDownload(StorageHelper.MIN_REQUIRED_SPACE)) {
            _downloadState.value = DownloadState.Error("Insufficient storage space")
            return
        }

        // Check Wi-Fi only setting
        val wifiOnlyEnabled = downloadPreferences.wifiOnlyDownloads.first()
        if (wifiOnlyEnabled && !isWifiConnected()) {
            _downloadState.value = DownloadState.RequiresWifi
            return
        }

        startDownload(movie.id, movie.videoUrl)
        markMovieAsDownloaded(movie.id)
    }

    fun startDownload(movieId: Int, url: String) {
        val mediaItem = MediaItem.fromUri(Uri.parse(url))
        downloadManager.getDownloadManager().addDownload(mediaItem)
        updateDownloadMap(movieId.toString(), 0f to DownloadStatus.QUEUED.name)
    }

    private fun updateDownloadMap(movieId: String, progressStatus: Pair<Float, String>) {
        val currentMap = _downloads.value.toMutableMap()
        currentMap[movieId] = progressStatus
        _downloads.value = currentMap
    }

    fun cancelDownload(movieId: String) {
        DownloadService.sendRemoveDownload(
            context,
            VideoDownloadService::class.java,
            movieId,
            false
        )

        progressMonitor.stopTracking(movieId.toInt())

        coroutineScope.launch {
            removeMovieFromDownloaded(movieId.toInt())
        }
    }

    suspend fun markMovieAsDownloaded(movieId: Int) {
        context.dataStore.edit { preferences ->
            val currentDownloads = preferences[DOWNLOADED_MOVIES]?.toMutableSet() ?: mutableSetOf()
            currentDownloads.add(movieId.toString())
            preferences[DOWNLOADED_MOVIES] = currentDownloads
        }
    }

    suspend fun removeMovieFromDownloaded(movieId: Int) {
        context.dataStore.edit { preferences ->
            val currentDownloads = preferences[DOWNLOADED_MOVIES]?.toMutableSet() ?: mutableSetOf()
            currentDownloads.remove(movieId.toString())
            preferences[DOWNLOADED_MOVIES] = currentDownloads
        }
    }

    suspend fun clearAllDownloads() {
        val downloadedIds = getDownloadedMovies().first()
        downloadedIds.forEach { movieId ->
            cancelDownload(movieId.toString())
        }
        storageHelper.clearDownloadedContent()
    }

    fun getTotalDownloadedSize(): Long {
        return storageHelper.getDownloadedContentSize()
    }

    fun getDownloadedMovies(): Flow<Set<Int>> {
        return context.dataStore.data.map { preferences ->
            preferences[DOWNLOADED_MOVIES]?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        }
    }

    private suspend fun resumePendingDownloads() {
        val wifiOnlyEnabled = downloadPreferences.wifiOnlyDownloads.first()
        if (wifiOnlyEnabled && !isWifiConnected()) {
            return
        }

        // Implement resume logic here using DownloadService
        _downloadState.value = DownloadState.Idle
    }

    private fun isWifiConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }

    fun getDownloadStatus(movieId: Int): DownloadStatus {
        val progress = VideoDownloadService.getProgress(movieId.toString())
        return when {
            progress == 0f -> DownloadStatus.NOT_DOWNLOADED
            progress > 0f && progress < 100f -> DownloadStatus.IN_PROGRESS
            progress == 100f -> DownloadStatus.COMPLETED
            progress == -1f -> DownloadStatus.FAILED
            else -> DownloadStatus.NOT_DOWNLOADED
        }
    }

    sealed class DownloadState {
        object Idle : DownloadState()
        object NoNetwork : DownloadState()
        object RequiresWifi : DownloadState()
        data class Downloading(val movieId: Int) : DownloadState()
        data class Error(val message: String) : DownloadState()
    }

    companion object {
        const val DOWNLOAD_NOTIFICATION_CHANNEL_ID = "downloads"
        const val DOWNLOAD_NOTIFICATION_ID = 1
    }
}