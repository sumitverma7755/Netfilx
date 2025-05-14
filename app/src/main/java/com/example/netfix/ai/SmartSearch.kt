package com.example.netfix.ai

import com.example.netfix.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * AI-powered smart search that understands natural language queries
 * and provides contextually relevant results.
 */
class SmartSearch {
    
    /**
     * Process natural language search queries and return relevant content
     * Examples:
     * - "Show me sci-fi movies from the 80s"
     * - "I want something funny and light-hearted"
     * - "Movies like Stranger Things"
     */
    suspend fun search(
        query: String,
        availableContent: List<Movie>
    ): List<Movie> = withContext(Dispatchers.IO) {
        // In a real implementation, this would use NLP to:
        // 1. Extract entities (genres, time periods, actors, etc.)
        // 2. Understand intent (recommendation, specific search)
        // 3. Match against content metadata
        
        // For demo purposes, we'll do simple keyword matching
        val normalizedQuery = query.lowercase()
        
        val results = availableContent.filter { movie ->
            val searchableText = """
                ${movie.title.lowercase()} 
                ${movie.description.lowercase()} 
                ${movie.category.lowercase()}
                ${movie.year}
            """.trimIndent()
            
            // Check if any keywords from the query match the content
            normalizedQuery.split(" ").any { keyword ->
                // Skip common words
                if (keyword.length <= 3 || STOP_WORDS.contains(keyword)) {
                    false
                } else {
                    searchableText.contains(keyword)
                }
            }
        }
        
        results
    }
    
    /**
     * Provides content recommendations based on similarity to a reference movie
     */
    suspend fun findSimilarContent(
        referenceMovie: Movie,
        availableContent: List<Movie>,
        limit: Int = 5
    ): List<Movie> = withContext(Dispatchers.IO) {
        // In a real implementation, this would use content embeddings
        // to find similar content based on themes, style, etc.
        
        // For demo purposes, we'll use simple similarity metrics
        availableContent
            .filter { it.id != referenceMovie.id }
            .sortedByDescending { movie ->
                calculateContentSimilarity(referenceMovie, movie)
            }
            .take(limit)
    }
    
    /**
     * Calculate similarity score between two movies
     */
    private fun calculateContentSimilarity(movie1: Movie, movie2: Movie): Float {
        var score = 0f
        
        // Same category/genre
        if (movie1.category == movie2.category) {
            score += 3f
        }
        
        // Similar time period (within 5 years)
        if (Math.abs(movie1.year - movie2.year) <= 5) {
            score += 2f
        }
        
        // Similar rating
        if (Math.abs(movie1.rating - movie2.rating) <= 1) {
            score += 1f
        }
        
        // Title or description similarity
        val movie1Words = movie1.title.lowercase().split(" ") + 
                          movie1.description.lowercase().split(" ")
        val movie2Words = movie2.title.lowercase().split(" ") + 
                          movie2.description.lowercase().split(" ")
        
        val commonWords = movie1Words.intersect(movie2Words.toSet())
        score += (commonWords.size * 0.5f).coerceAtMost(4f)
        
        return score
    }
    
    companion object {
        private val STOP_WORDS = setOf(
            "the", "and", "for", "with", "from", "this", "that", 
            "but", "not", "are", "was", "were", "have", "has"
        )
    }
}
