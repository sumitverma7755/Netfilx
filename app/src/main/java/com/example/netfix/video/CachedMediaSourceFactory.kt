package com.example.netfix.video

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import java.io.File

class CachedMediaSourceFactory(private val context: Context) {
    private val cache: SimpleCache by lazy {
        SimpleCache(
            File(context.cacheDir, "media"),
            NoOpCacheEvictor(),
            StandaloneDatabaseProvider(context)
        )
    }

    fun createMediaSource(uri: String): MediaSource {
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)
            .setConnectTimeoutMs(DEFAULT_TIMEOUT_MS)
            .setReadTimeoutMs(DEFAULT_TIMEOUT_MS)

        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setCacheWriteDataSinkFactory(null)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

        val mediaItem = MediaItem.fromUri(uri)
        return DefaultMediaSourceFactory(cacheDataSourceFactory)
            .createMediaSource(mediaItem)
    }

    companion object {
        private const val DEFAULT_TIMEOUT_MS = 15_000
    }
}