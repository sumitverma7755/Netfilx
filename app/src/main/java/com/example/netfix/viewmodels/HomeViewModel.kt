package com.example.netfix.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netfix.model.Movie
import com.example.netfix.repository.MovieRepository
import com.example.netfix.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _moviesState = MutableStateFlow<Resource<List<Movie>>>(Resource.Loading())
    val moviesState: StateFlow<Resource<List<Movie>>> = _moviesState.asStateFlow()

    private val _featuredMovieState = MutableStateFlow<Resource<Movie>>(Resource.Loading())
    val featuredMovieState: StateFlow<Resource<Movie>> = _featuredMovieState.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _moviesState.value = Resource.Loading()
            try {
                val response = repository.getMovies()
                if (response.isSuccessful) {
                    val movies = response.body() ?: emptyList()
                    _moviesState.value = Resource.Success(movies)
                    
                    // Set first movie as featured
                    if (movies.isNotEmpty()) {
                        _featuredMovieState.value = Resource.Success(movies.first())
                    }
                } else {
                    _moviesState.value = Resource.Error("Failed to load movies")
                }
            } catch (e: Exception) {
                _moviesState.value = Resource.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun refreshMovies() {
        loadMovies()
    }
}
