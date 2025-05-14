package com.example.netfix

import com.example.netfix.model.Movie
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET("movies")
    suspend fun getMovies(): Response<List<Movie>>

    @GET("movies/{id}")
    suspend fun getMovieDetails(@Path("id") id: String): Response<Movie>

    @GET("movies/search")
    suspend fun searchMovies(@Query("query") query: String): Response<List<Movie>>

    @GET("movies/category/{category}")
    suspend fun getMoviesByCategory(@Path("category") category: String): Response<List<Movie>>
}

object MovieApi {
    private const val BASE_URL = "https://api.example.com/"

    val instance: MovieApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }
}
