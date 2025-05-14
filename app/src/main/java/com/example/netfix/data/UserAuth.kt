package com.example.netfix.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore instance at the app level
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

/**
 * UserAuth class to manage user authentication state
 */
class UserAuth(private val context: Context) {
    
    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_ID = stringPreferencesKey("user_id")
    }
    
    // Get the current login state
    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }
    
    // Get the current user email
    val userEmail: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_EMAIL] ?: ""
        }
    
    // Get the current user ID
    val userId: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID] ?: ""
        }
    
    // Save login state
    suspend fun saveLoginState(isLoggedIn: Boolean, email: String, userId: String) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
            preferences[USER_EMAIL] = email
            preferences[USER_ID] = userId
        }
    }
    
    // Clear login state (logout)
    suspend fun clearLoginState() {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false
            preferences[USER_EMAIL] = ""
            preferences[USER_ID] = ""
        }
    }
}
