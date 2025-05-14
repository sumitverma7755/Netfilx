package com.example.netfix.video

import android.content.Context
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

object VideoCache {
    private var cache: SimpleCache? = null
    private const val MAX_CACHE_SIZE = 1024 * 1024 * 1024L // 1GB cache

    @Synchronized
    fun getInstance(context: Context): SimpleCache {
        if (cache == null) {
            val cacheDir = File(context.cacheDir, "video_cache")
            val databaseProvider = StandaloneDatabaseProvider(context)
            cache = SimpleCache(
                cacheDir,
                LeastRecentlyUsedCacheEvictor(MAX_CACHE_SIZE),
                databaseProvider
            )
        }
        return cache!!
    }
}