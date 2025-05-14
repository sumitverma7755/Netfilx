package com.example.netfix.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.netfix.ai.ContentPersonalization
import com.example.netfix.components.*
import com.example.netfix.model.Movie
import com.example.netfix.model.MovieCategory
import com.example.netfix.model.UserProfile
import com.example.netfix.util.Resource
import com.example.netfix.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    onMovieClick: (Movie) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val moviesState = viewModel.moviesState.collectAsState()
    val featuredMovieState = viewModel.featuredMovieState.collectAsState()
    
    // AI personalization engine
    val personalization = remember { ContentPersonalization() }
    
    // Create a default user profile for personalization
    val defaultUserProfile = remember {
        UserProfile(
            id = "default",
            name = "User",
            preferredGenres = listOf("Action", "Drama", "Sci-Fi")
        )
    }
    
    // State to hold AI-personalized categories
    val categories = when (moviesState.value) {
        is Resource.Success -> {
            val movies = (moviesState.value as Resource.Success).data
            personalization.getPersonalizedCategories(defaultUserProfile, movies)
        }
        else -> emptyList()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (featuredMovieState.value) {
            is Resource.Success -> {
                item {
                    FeaturedMovieBanner(
                        featuredMovie = (featuredMovieState.value as Resource.Success).data,
                        onPlayClick = { onMovieClick((featuredMovieState.value as Resource.Success).data) }
                    )
                }
            }
            is Resource.Error -> {
                item {
                    Text(
                        text = "Couldn't load featured content",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            is Resource.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        when (moviesState.value) {
            is Resource.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            is Resource.Error -> {
                item {
                    Text(
                        text = "Error loading movies: ${(moviesState.value as Resource.Error).message}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            is Resource.Success -> {
                items(categories) { category ->
                    MovieRow(
                        title = category.name,
                        movies = category.movies,
                        modifier = Modifier.padding(top = 16.dp),
                        onMovieClick = onMovieClick
                    )
                }
            }
        }
    }
}
