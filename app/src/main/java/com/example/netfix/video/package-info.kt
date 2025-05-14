/**
 * Video download and playback functionality for Netfix app.
 *
 * This package contains the core components for video downloading and offline playback:
 *
 * Key Components:
 * - [VideoDownloadManager]: Central manager for handling video downloads
 * - [VideoDownloadService]: Background service for download operations
 * - [MediaCacheConfig]: Configuration for ExoPlayer media caching
 * - [DownloadProgressMonitor]: Tracks and reports download progress
 * - [CachedMediaSourceFactory]: Creates cached media sources for playback
 *
 * Features:
 * - Background downloads with progress tracking
 * - Wi-Fi only download option
 * - Auto-deletion of watched content
 * - Offline playback support
 * - Download state persistence
 * - ExoPlayer integration with caching
 *
 * Usage example:
 * ```
 * val downloadManager = VideoDownloadManager(context)
 * 
 * // Start a download
 * downloadManager.downloadMovie(movie)
 * 
 * // Track download progress
 * downloadManager.downloadProgress.collect { progress ->
 *     // Update UI with progress
 * }
 * 
 * // Listen for download state changes
 * downloadManager.downloadState.collect { state ->
 *     when (state) {
 *         is DownloadState.Downloading -> // Show progress
 *         is DownloadState.Completed -> // Show completion
 *         is DownloadState.Error -> // Handle error
 *     }
 * }
 * ```
 *
 * Note: This implementation uses ExoPlayer for video playback and downloading.
 * Network connectivity and storage permissions are required.
 */
package com.example.netfix.video