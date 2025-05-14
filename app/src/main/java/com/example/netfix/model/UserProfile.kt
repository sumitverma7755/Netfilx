package com.example.netfix.model

/**
 * Represents a user profile with viewing preferences
 */
data class UserProfile(
    val id: String,
    val name: String,
    val preferredGenres: List<String> = emptyList()
)
