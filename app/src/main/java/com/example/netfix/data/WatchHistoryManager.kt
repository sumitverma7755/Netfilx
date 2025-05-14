package com.example.netfix.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.netfix.model.Movie
import com.example.netfix.video.VideoDownloadManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

private val Context.historyDataStore by preferencesDataStore(name = "watch_history")

class WatchHistoryManager(
    private val context: Context,
    private val downloadManager: VideoDownloadManager
) {
    private val WATCHED_MOVIES = stringPreferencesKey("watched_movies")

    suspend fun markAsWatched(movie: Movie) {
        context.historyDataStore.edit { preferences ->
            val currentHistory = preferences[WATCHED_MOVIES]?.split(",")?.toMutableSet() ?: mutableSetOf()
            currentHistory.add(movie.id.toString())
            preferences[WATCHED_MOVIES] = currentHistory.joinToString(",")
        }

        // If auto-delete is enabled and movie is downloaded, remove the download
        if (downloadManager.autoDeleteEnabled.first()) {
            downloadManager.cancelDownload(movie.id.toString())
        }
    }

    fun getWatchedMovies(): Flow<Set<Int>> {
        return context.historyDataStore.data.map { preferences ->
            preferences[WATCHED_MOVIES]?.split(",")
                ?.filter { it.isNotEmpty() }
                ?.mapNotNull { it.toIntOrNull() }
                ?.toSet()
                ?: emptySet()
        }
    }
}