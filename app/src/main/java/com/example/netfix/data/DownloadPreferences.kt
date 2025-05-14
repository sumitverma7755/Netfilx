package com.example.netfix.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.downloadPreferences: DataStore<Preferences> by preferencesDataStore(
    name = "download_preferences"
)

class DownloadPreferences(private val context: Context) {
    private val wifiOnlyKey = booleanPreferencesKey("wifi_only")
    private val autoDeleteKey = booleanPreferencesKey("auto_delete_watched")

    val wifiOnlyDownloads: Flow<Boolean> = context.downloadPreferences.data
        .map { preferences ->
            preferences[wifiOnlyKey] ?: true
        }

    val autoDeleteWatched: Flow<Boolean> = context.downloadPreferences.data
        .map { preferences ->
            preferences[autoDeleteKey] ?: false
        }

    suspend fun setWifiOnly(enabled: Boolean) {
        context.downloadPreferences.edit { preferences ->
            preferences[wifiOnlyKey] = enabled
        }
    }

    suspend fun setAutoDelete(enabled: Boolean) {
        context.downloadPreferences.edit { preferences ->
            preferences[autoDeleteKey] = enabled
        }
    }
}