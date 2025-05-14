package com.example.netfix.model

/**
 * User data class to store user information
 */
data class User(
    val id: String,
    val email: String,
    val password: String,
    val name: String,
    val profiles: List<UserProfile> = emptyList()
)

/**
 * In-memory database for users
 */
object UserDatabase {
    private val users = mutableListOf<User>()
    
    init {
        // Add some sample users
        users.add(
            User(
                id = "user1",
                email = "demo@example.com",
                password = "password",
                name = "Demo User",
                profiles = listOf(
                    UserProfile(
                        id = "profile1",
                        name = "Main",
                        preferredGenres = listOf("Action", "Sci-Fi")
                    ),
                    UserProfile(
                        id = "profile2",
                        name = "Kids",
                        preferredGenres = listOf("Animation", "Family")
                    )
                )
            )
        )
    }
    
    fun getUsers(): List<User> = users
    
    fun getUserByEmail(email: String): User? {
        return users.find { it.email.equals(email, ignoreCase = true) }
    }
    
    fun getUserById(id: String): User? {
        return users.find { it.id == id }
    }
    
    fun addUser(user: User) {
        users.add(user)
    }
    
    fun validateCredentials(email: String, password: String): User? {
        return users.find { 
            it.email.equals(email, ignoreCase = true) && it.password == password 
        }
    }
}
