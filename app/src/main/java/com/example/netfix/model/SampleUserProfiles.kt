package com.example.netfix.model

/**
 * Sample user profiles for testing UI
 */
object SampleUserProfiles {
    val currentUser = UserProfile(
        id = "user1",
        name = "Alex",
        preferredGenres = listOf("Sci-Fi", "Drama", "Thriller")
    )
    
    val profiles = listOf(
        currentUser,
        UserProfile(
            id = "user2",
            name = "Jamie",
            preferredGenres = listOf("Comedy", "Action")
        ),
        UserProfile(
            id = "user3",
            name = "Kids",
            preferredGenres = listOf("Animation", "Family")
        ),
        UserProfile(
            id = "add_profile",
            name = "Add Profile",
            preferredGenres = emptyList()
        )
    )
}
