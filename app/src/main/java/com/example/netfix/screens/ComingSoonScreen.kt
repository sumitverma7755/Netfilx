package com.example.netfix.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.netfix.data.movies
import com.example.netfix.model.Movie
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Screen that shows upcoming content and new releases
 */
@Composable
fun ComingSoonScreen() {
    val upcomingMovies = remember {
        // For demo purposes, we'll use the existing movies but pretend they're upcoming
        movies.mapIndexed { index, movie ->
            movie to LocalDate.now().plusDays((index + 1) * 7L)
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "New & Hot",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Notifications toggle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Icon(
                Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = "Notify me about new releases",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            var notificationsEnabled by remember { mutableStateOf(true) }
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.DarkGray
                )
            )
        }
        
        // Coming soon list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(upcomingMovies) { (movie, releaseDate) ->
                ComingSoonItem(
                    movie = movie,
                    releaseDate = releaseDate
                )
            }
        }
    }
}

@Composable
fun ComingSoonItem(
    movie: Movie,
    releaseDate: LocalDate
) {
    val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
    
    Column {
        // Release date
        Text(
            text = "Coming on ${releaseDate.format(formatter)}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Movie thumbnail
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.DarkGray)
        ) {
            // In a real app, we would load the movie thumbnail here
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Movie title
        Text(
            text = movie.title,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Movie description
        Text(
            text = movie.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.LightGray,
            maxLines = 3
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Remind me button
        Button(
            onClick = { /* Set reminder */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(
                Icons.Default.Notifications,
                contentDescription = "Remind me",
                modifier = Modifier.size(16.dp)
            )
            
            Spacer(modifier = Modifier.width(4.dp))
            
            Text("Remind Me")
        }
    }
}
