package com.example.netfix.ai

import com.example.netfix.model.Movie
import com.example.netfix.model.MovieCategory
import com.example.netfix.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalTime

/**
 * AI-powered content personalization service that adapts the UI
 * and content presentation based on user behavior and preferences.
 */
class ContentPersonalization {
    
    /**
     * Generate personalized content categories for the home screen
     * based on user profile and time of day.
     */
    suspend fun getPersonalizedCategories(
        userProfile: UserProfile,
        allMovies: List<Movie>
    ): List<MovieCategory> = withContext(Dispatchers.IO) {
        val categories = mutableListOf<MovieCategory>()
        
        // Continue Watching (simulated)
        val simulatedWatchHistory = allMovies.take(3)
        if (simulatedWatchHistory.isNotEmpty()) {
            categories.add(MovieCategory(
                name = "Continue Watching",
                movies = simulatedWatchHistory
            ))
        }
        
        // Time-based recommendations
        val currentTime = LocalTime.now()
        val timeBasedCategory = when {
            currentTime.isBefore(LocalTime.of(12, 0)) -> "Morning Picks"
            currentTime.isBefore(LocalTime.of(17, 0)) -> "Afternoon Selections"
            currentTime.isBefore(LocalTime.of(22, 0)) -> "Evening Watchlist"
            else -> "Late Night Viewing"
        }
        
        categories.add(MovieCategory(
            name = timeBasedCategory,
            movies = getTimeBasedRecommendations(userProfile, allMovies, currentTime)
        ))
        
        // Genre-based categories from user preferences
        userProfile.preferredGenres.forEach { genre ->
            val genreMovies = allMovies.filter { it.category == genre }
            if (genreMovies.isNotEmpty()) {
                categories.add(MovieCategory(
                    name = "Top $genre for You",
                    movies = genreMovies.take(10)
                ))
            }
        }
        
        // Trending Now (generic)
        categories.add(MovieCategory(
            name = "Trending Now",
            movies = allMovies.sortedByDescending { it.rating }.take(10)
        ))
        
        // New Releases
        categories.add(MovieCategory(
            name = "New Releases",
            movies = allMovies.sortedByDescending { it.year }.take(10)
        ))
        
        // Similar to recently watched (simulated)
        if (simulatedWatchHistory.isNotEmpty()) {
            val recentlyWatched = simulatedWatchHistory.first()
            val similarContent = allMovies.filter { 
                it.id != recentlyWatched.id && it.category == recentlyWatched.category 
            }
            
            if (similarContent.isNotEmpty()) {
                categories.add(MovieCategory(
                    name = "Because You Watched ${recentlyWatched.title}",
                    movies = similarContent.take(10)
                ))
            }
        }
        
        categories
    }
    
    /**
     * Get recommendations based on time of day and user preferences
     */
    private fun getTimeBasedRecommendations(
        userProfile: UserProfile,
        allMovies: List<Movie>,
        currentTime: LocalTime
    ): List<Movie> {
        // For demo purposes, use simple rules based on time of day and user preferences
        val preferredGenres = userProfile.preferredGenres
        
        return when {
            currentTime.isBefore(LocalTime.of(12, 0)) -> {
                // Morning: shorter, lighter content
                allMovies.filter { 
                    it.duration.contains("30m") || it.category in listOf("Comedy", "Documentary")
                }
            }
            currentTime.isBefore(LocalTime.of(17, 0)) -> {
                // Afternoon: mixed content with preference for user's genres
                allMovies.filter { it.category in preferredGenres }.ifEmpty { allMovies.shuffled() }
            }
            currentTime.isBefore(LocalTime.of(22, 0)) -> {
                // Evening: popular content, longer shows, preferred genres
                val eveningMovies = allMovies.filter { it.category in preferredGenres }
                eveningMovies.sortedByDescending { it.rating }.ifEmpty { 
                    allMovies.sortedByDescending { it.rating } 
                }
            }
            else -> {
                // Late night: thrillers, sci-fi, mature content
                val lateNightGenres = listOf("Thriller", "Sci-Fi", "Horror", "Drama")
                val preferredLateNight = preferredGenres.intersect(lateNightGenres.toSet())
                
                if (preferredLateNight.isNotEmpty()) {
                    allMovies.filter { it.category in preferredLateNight }
                } else {
                    allMovies.filter { it.category in lateNightGenres }
                }
            }
        }.take(10)
    }
    
    /**
     * Generate viewing insights based on user preferences
     */
    suspend fun generateViewingInsights(
        userProfile: UserProfile
    ): ViewingInsights = withContext(Dispatchers.IO) {
        // For demo purposes, return simulated insights
        ViewingInsights(
            optimalViewingTimes = listOf("7:00 PM", "9:30 PM"),
            preferredDuration = 45, // Default average viewing time in minutes
            preferredDays = listOf("Friday", "Saturday", "Sunday"),
            contentCompletionRate = 0.8f // Default completion rate
        )
    }
}

/**
 * Insights derived from AI analysis of viewing patterns
 */
data class ViewingInsights(
    val optimalViewingTimes: List<String>,
    val preferredDuration: Int,
    val preferredDays: List<String>,
    val contentCompletionRate: Float
)
