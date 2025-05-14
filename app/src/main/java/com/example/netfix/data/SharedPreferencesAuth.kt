package com.example.netfix.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Authentication manager using SharedPreferences instead of DataStore
 * This is a simpler implementation that doesn't require additional dependencies
 */
class SharedPreferencesAuth(context: Context) {
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )
    
    // StateFlows to observe authentication state
    private val _isLoggedInFlow = MutableStateFlow(isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedInFlow
    
    private val _userEmailFlow = MutableStateFlow(getUserEmail())
    val userEmail: StateFlow<String> = _userEmailFlow
    
    private val _userIdFlow = MutableStateFlow(getUserId())
    val userId: StateFlow<String> = _userIdFlow
    
    // Check if user is logged in
    private fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    // Get current user email
    private fun getUserEmail(): String {
        return sharedPreferences.getString(KEY_USER_EMAIL, "") ?: ""
    }
    
    // Get current user ID
    private fun getUserId(): String {
        return sharedPreferences.getString(KEY_USER_ID, "") ?: ""
    }
    
    // Save login state
    fun saveLoginState(isLoggedIn: Boolean, email: String, userId: String) {
        sharedPreferences.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_ID, userId)
            apply()
        }
        
        // Update flows
        _isLoggedInFlow.value = isLoggedIn
        _userEmailFlow.value = email
        _userIdFlow.value = userId
    }
    
    // Clear login state (logout)
    fun clearLoginState() {
        sharedPreferences.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, false)
            putString(KEY_USER_EMAIL, "")
            putString(KEY_USER_ID, "")
            apply()
        }
        
        // Update flows
        _isLoggedInFlow.value = false
        _userEmailFlow.value = ""
        _userIdFlow.value = ""
    }
    
    companion object {
        private const val PREFS_NAME = "netfix_prefs"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_ID = "user_id"
    }
}
