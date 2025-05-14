package com.example.netfix.video

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.scheduler.Requirements
import androidx.media3.exoplayer.scheduler.Scheduler
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import com.example.netfix.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@UnstableApi
class VideoDownloadService : DownloadService(
    FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    DOWNLOAD_NOTIFICATION_CHANNEL_ID,
    R.string.exo_download_notification_channel_name,
    0
) {
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val progressMap = MutableStateFlow<Map<String, Float>>(emptyMap())
    
    override fun getDownloadManager(): DownloadManager {
        val downloadCache = MediaCacheConfig.getDownloadCache(this)
        val dataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)
            .setConnectTimeoutMs(15000)
            .setReadTimeoutMs(15000)

        return DownloadManager(
            this,
            StandaloneDatabaseProvider(this),
            downloadCache,
            dataSourceFactory,
            Scheduler.INSTANCE
        ).apply {
            maxParallelDownloads = 3
            addListener(object : DownloadManager.Listener {
                override fun onDownloadChanged(
                    downloadManager: DownloadManager,
                    download: Download,
                    finalException: Exception?
                ) {
                    val movieId = download.request.id
                    when (download.state) {
                        Download.STATE_DOWNLOADING -> {
                            updateProgress(movieId, download.percentDownloaded)
                        }
                        Download.STATE_COMPLETED -> {
                            updateProgress(movieId, 100f)
                            serviceScope.launch {
                                VideoDownloadManager(applicationContext)
                                    .markMovieAsDownloaded(movieId.toInt())
                            }
                        }
                        Download.STATE_FAILED -> {
                            updateProgress(movieId, -1f)
                            serviceScope.launch {
                                VideoDownloadManager(applicationContext)
                                    .removeMovieFromDownloaded(movieId.toInt())
                            }
                        }
                        else -> {}
                    }
                }

                override fun onDownloadRemoved(
                    downloadManager: DownloadManager,
                    download: Download
                ) {
                    val movieId = download.request.id
                    removeProgress(movieId)
                    serviceScope.launch {
                        VideoDownloadManager(applicationContext)
                            .removeMovieFromDownloaded(movieId.toInt())
                    }
                }
            })
        }
    }

    override fun getScheduler(): Scheduler? = null

    override fun getForegroundNotification(downloads: List<Download>, notMetRequirements: Int): Notification {
        createNotificationChannel()
        
        val inProgress = downloads.count { it.state == Download.STATE_DOWNLOADING }
        val completed = downloads.count { it.state == Download.STATE_COMPLETED }
        val failed = downloads.count { it.state == Download.STATE_FAILED }
        
        val message = when {
            inProgress > 0 -> {
                val totalProgress = downloads
                    .filter { it.state == Download.STATE_DOWNLOADING }
                    .map { it.percentDownloaded }
                    .average()
                "Downloading ${String.format("%.1f", totalProgress)}%"
            }
            completed > 0 -> "$completed downloads completed"
            failed > 0 -> "$failed downloads failed"
            else -> "Download service running"
        }

        return NotificationCompat.Builder(this, DOWNLOAD_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_download)
            .setContentTitle(getString(R.string.download_notification_title))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.exo_download_notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(
                DOWNLOAD_NOTIFICATION_CHANNEL_ID,
                name,
                importance
            ).apply {
                setShowBadge(false)
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateProgress(movieId: String, progress: Float) {
        val currentMap = progressMap.value.toMutableMap()
        currentMap[movieId] = progress
        progressMap.value = currentMap
    }

    private fun removeProgress(movieId: String) {
        val currentMap = progressMap.value.toMutableMap()
        currentMap.remove(movieId)
        progressMap.value = currentMap
    }

    companion object {
        private const val FOREGROUND_NOTIFICATION_ID = 1
        private const val DOWNLOAD_NOTIFICATION_CHANNEL_ID = "downloads"

        fun getProgress(movieId: String): Float {
            return instance?.progressMap?.value?.get(movieId) ?: 0f
        }

        fun getDownloadService(context: Context): VideoDownloadService {
            return VideoDownloadService()
        }

        private var instance: VideoDownloadService? = null

        fun getInstance(): VideoDownloadService? = instance
    }

    init {
        instance = this
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}