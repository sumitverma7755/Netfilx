package com.example.netfix.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.netfix.ai.SmartSearch
import com.example.netfix.components.MovieItem
import com.example.netfix.data.movies
import com.example.netfix.model.Movie
import kotlinx.coroutines.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onMovieClick: (Movie) -> Unit = {}
) {
    val (query, setQuery) = remember { mutableStateOf("") }
    val (searchResults, setSearchResults) = remember { mutableStateOf<List<Movie>>(emptyList()) }
    val (isSearching, setIsSearching) = remember { mutableStateOf(false) }
    val (searchPerformed, setSearchPerformed) = remember { mutableStateOf(false) }
    
    val smartSearch = remember { SmartSearch() }
    val coroutineScope = rememberCoroutineScope()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Search",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Search bar with AI capabilities
        OutlinedTextField(
            value = query,
            onValueChange = { setQuery(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search by title, genre, or describe what you want to watch") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                focusedContainerColor = Color.DarkGray,
                unfocusedContainerColor = Color.DarkGray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedPlaceholderColor = Color.LightGray,
                unfocusedPlaceholderColor = Color.LightGray
            )
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // AI search suggestions
        if (query.length >= 3 && !searchPerformed) {
            Text(
                text = "Try natural language like:",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray,
                modifier = Modifier.padding(start = 8.dp)
            )
            
            val suggestions = listOf(
                "\"Show me sci-fi movies\"",
                "\"Something with drama and suspense\"",
                "\"Movies like ${movies.random().title}\""
            )
            
            suggestions.forEach { suggestion ->
                TextButton(
                    onClick = { 
                        setQuery(suggestion.removeSurrounding("\""))
                        coroutineScope.launch {
                            performSearch(
                                suggestion.removeSurrounding("\""),
                                smartSearch,
                                setIsSearching,
                                setSearchResults,
                                setSearchPerformed
                            )
                        }
                    },
                    modifier = Modifier.padding(vertical = 4.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(suggestion)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Search button
        Button(
            onClick = {
                coroutineScope.launch {
                    performSearch(
                        query,
                        smartSearch,
                        setIsSearching,
                        setSearchResults,
                        setSearchPerformed
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = query.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            Text("Search")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Results
        if (isSearching) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else if (searchPerformed) {
            if (searchResults.isEmpty()) {
                Text(
                    text = "No results found. Try a different search.",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                )
            } else {
                Text(
                    text = "AI found ${searchResults.size} results for \"$query\"",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                LazyColumn {
                    items(searchResults) { movie ->
                        MovieItem(
                            movie = movie,
                            onMovieClick = onMovieClick
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

private suspend fun performSearch(
    query: String,
    smartSearch: SmartSearch,
    setIsSearching: (Boolean) -> Unit,
    setSearchResults: (List<Movie>) -> Unit,
    setSearchPerformed: (Boolean) -> Unit
) {
    if (query.isBlank()) return
    
    setIsSearching(true)
    setSearchPerformed(false)
    
    val results = withContext(Dispatchers.IO) {
        smartSearch.search(query, movies)
    }
    
    setSearchResults(results)
    setIsSearching(false)
    setSearchPerformed(true)
}
