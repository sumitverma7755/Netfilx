package com.example.netfix.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.netfix.model.Movie

@Composable
fun MovieRow(
    title: String,
    movies: List<Movie>,
    modifier: Modifier = Modifier,
    onMovieClick: (Movie) -> Unit = {}
) {
    Column(modifier = modifier) {
        // Category title
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        // Row of movie posters
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(movies) { movie ->
                MoviePoster(
                    movie = movie,
                    onClick = { onMovieClick(movie) }
                )
            }
        }
    }
}
