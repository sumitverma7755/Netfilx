package com.example.netfix.video

import android.content.Context
import android.os.Environment
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

object MediaCacheConfig {
    private const val MAX_CACHE_SIZE = 5L * 1024 * 1024 * 1024 // 5GB
    private const val DOWNLOADS_FOLDER = "netfix_downloads"
    private var downloadCache: Cache? = null
    private var databaseProvider: DatabaseProvider? = null

    @Synchronized
    fun getDownloadCache(context: Context): Cache {
        if (downloadCache == null) {
            databaseProvider = StandaloneDatabaseProvider(context)
            val downloadFolder = getDownloadDirectory(context)
            downloadCache = SimpleCache(
                downloadFolder,
                LeastRecentlyUsedCacheEvictor(MAX_CACHE_SIZE),
                databaseProvider!!
            )
        }
        return downloadCache!!
    }

    private fun getDownloadDirectory(context: Context): File {
        val downloads = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        } else {
            context.filesDir
        }
        return File(downloads, DOWNLOADS_FOLDER).also { it.mkdirs() }
    }

    fun clearCache(context: Context) {
        downloadCache?.release()
        downloadCache = null
        databaseProvider = null
        getDownloadDirectory(context).deleteRecursively()
    }
}