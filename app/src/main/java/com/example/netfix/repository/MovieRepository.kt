package com.example.netfix.repository

import com.example.netfix.MovieApi
import com.example.netfix.model.Movie
import retrofit2.Response

class MovieRepository {
    private val movieApiService = MovieApi.instance

    suspend fun getMovies(): Response<List<Movie>> {
        return movieApiService.getMovies()
    }

    suspend fun getMovieDetails(id: String): Response<Movie> {
        return movieApiService.getMovieDetails(id)
    }

    suspend fun searchMovies(query: String): Response<List<Movie>> {
        return movieApiService.searchMovies(query)
    }

    suspend fun getMoviesByCategory(category: String): Response<List<Movie>> {
        return movieApiService.getMoviesByCategory(category)
    }
}
