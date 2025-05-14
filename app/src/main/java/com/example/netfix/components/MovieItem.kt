package com.example.netfix.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.netfix.model.Movie

@Composable
fun MovieItem(
    movie: Movie,
    onMovieClick: (Movie) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onMovieClick(movie) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            // Thumbnail
            AsyncImage(
                model = movie.thumbnailUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
            )
            
            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "${movie.year} • ${movie.category} • ${movie.duration}",
                    style = MaterialTheme.typography.bodySmall
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = movie.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
