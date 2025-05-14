package com.example.netfix.util

import android.content.Context
import android.os.StatFs
import java.io.File

class StorageHelper(private val context: Context) {
    fun getAvailableStorage(): Long {
        val path = context.getExternalFilesDir(null)?.path ?: return 0L
        val stats = StatFs(path)
        return stats.availableBlocksLong * stats.blockSizeLong
    }

    fun getDownloadedContentSize(): Long {
        val downloadDir = File(context.getExternalFilesDir(null), "netfix_downloads")
        return if (downloadDir.exists()) {
            downloadDir.walkBottomUp()
                .filter { it.isFile }
                .map { it.length() }
                .sum()
        } else 0L
    }

    fun hasEnoughSpaceForDownload(requiredBytes: Long): Boolean {
        val available = getAvailableStorage()
        // Add 10% buffer for safety
        return available > (requiredBytes * 1.1)
    }

    fun clearDownloadedContent() {
        val downloadDir = File(context.getExternalFilesDir(null), "netfix_downloads")
        if (downloadDir.exists()) {
            downloadDir.deleteRecursively()
        }
    }

    companion object {
        const val MIN_REQUIRED_SPACE = 100 * 1024 * 1024L // 100MB minimum free space
    }
}