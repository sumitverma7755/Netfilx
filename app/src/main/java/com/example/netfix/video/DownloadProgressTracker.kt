package com.example.netfix.video

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DownloadProgressTracker {
    private val _downloadProgress = MutableStateFlow<Map<Int, Float>>(emptyMap())
    val downloadProgress: StateFlow<Map<Int, Float>> = _downloadProgress.asStateFlow()

    private val _activeDownloads = MutableStateFlow<Set<Int>>(emptySet())
    val activeDownloads: StateFlow<Set<Int>> = _activeDownloads.asStateFlow()

    fun updateProgress(movieId: Int, progress: Float) {
        val currentProgress = _downloadProgress.value.toMutableMap()
        currentProgress[movieId] = progress
        _downloadProgress.value = currentProgress
        
        // Update active downloads
        if (progress >= 1f) {
            removeActiveDownload(movieId)
        } else {
            addActiveDownload(movieId)
        }
    }

    fun addActiveDownload(movieId: Int) {
        val current = _activeDownloads.value.toMutableSet()
        current.add(movieId)
        _activeDownloads.value = current
    }

    fun removeActiveDownload(movieId: Int) {
        val current = _activeDownloads.value.toMutableSet()
        current.remove(movieId)
        _activeDownloads.value = current
        
        // Clear progress
        val currentProgress = _downloadProgress.value.toMutableMap()
        currentProgress.remove(movieId)
        _downloadProgress.value = currentProgress
    }

    fun getProgress(movieId: Int): Float {
        return _downloadProgress.value[movieId] ?: 0f
    }

    fun isDownloading(movieId: Int): Boolean {
        return movieId in _activeDownloads.value
    }
}