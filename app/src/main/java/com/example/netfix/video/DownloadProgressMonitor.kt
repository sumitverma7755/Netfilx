package com.example.netfix.video

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DownloadProgressMonitor {
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val _downloadProgress = MutableStateFlow<Map<Int, Float>>(emptyMap())
    val downloadProgress: StateFlow<Map<Int, Float>> = _downloadProgress.asStateFlow()

    init {
        // Start monitoring progress
        scope.launch {
            while (true) {
                updateProgress()
                delay(1000) // Update every second
            }
        }
    }

    private fun updateProgress() {
        val service = VideoDownloadService.getInstance() ?: return
        val currentProgress = _downloadProgress.value.toMutableMap()
        
        // Update progress for each movie ID in our map
        currentProgress.keys.forEach { movieId ->
            val progress = VideoDownloadService.getProgress(movieId.toString())
            if (progress >= 0) {
                currentProgress[movieId] = progress / 100f // Convert to 0-1 range
            } else {
                // Remove completed or failed downloads
                currentProgress.remove(movieId)
            }
        }
        
        _downloadProgress.value = currentProgress
    }

    fun startTracking(movieId: Int) {
        val currentProgress = _downloadProgress.value.toMutableMap()
        currentProgress[movieId] = 0f
        _downloadProgress.value = currentProgress
    }

    fun stopTracking(movieId: Int) {
        val currentProgress = _downloadProgress.value.toMutableMap()
        currentProgress.remove(movieId)
        _downloadProgress.value = currentProgress
    }

    companion object {
        private var instance: DownloadProgressMonitor? = null

        @Synchronized
        fun getInstance(): DownloadProgressMonitor {
            if (instance == null) {
                instance = DownloadProgressMonitor()
            }
            return instance!!
        }
    }
}