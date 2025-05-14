package com.example.netfix.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.netfix.components.MovieItem
import com.example.netfix.data.movies
import com.example.netfix.model.Movie
import com.example.netfix.video.VideoDownloadManager
import kotlinx.coroutines.launch

@Composable
fun DownloadsScreen(
    downloadManager: VideoDownloadManager,
    onMovieClick: (Movie) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    
    var downloadedMovieIds by remember { mutableStateOf<Set<Int>>(emptySet()) }
    val downloadProgress by downloadManager.downloadProgress.collectAsState()
    val downloadState by downloadManager.downloadState.collectAsState()
    
    // Collect downloaded movie IDs
    LaunchedEffect(Unit) {
        downloadManager.getDownloadedMovies().collect { ids ->
            downloadedMovieIds = ids
        }
    }
    
    // Filter movies that are downloaded or currently downloading
    val activeDownloads = movies.filter { movie -> 
        movie.id in downloadProgress.keys || movie.id in downloadedMovieIds
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Downloads",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        if (activeDownloads.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Download,
                        contentDescription = "No Downloads",
                        tint = Color.Gray,
                        modifier = Modifier.size(80.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "No downloads yet",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Movies and shows you download appear here for offline viewing",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Downloads sections
            LazyColumn {
                // Active downloads section
                val downloading = activeDownloads.filter { it.id in downloadProgress.keys }
                if (downloading.isNotEmpty()) {
                    item {
                        Text(
                            text = "Downloading",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    
                    items(downloading) { movie ->
                        MovieDownloadItem(
                            movie = movie,
                            progress = downloadProgress[movie.id] ?: 0f,
                            onMovieClick = onMovieClick,
                            onCancelClick = {
                                coroutineScope.launch {
                                    downloadManager.cancelDownload(movie)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    item {
                        Divider(
                            color = Color.DarkGray,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }
                
                // Downloaded content section
                val downloaded = activeDownloads.filter { it.id in downloadedMovieIds }
                if (downloaded.isNotEmpty()) {
                    item {
                        Text(
                            text = "Available for offline viewing",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    
                    items(downloaded) { movie ->
                        MovieDownloadItem(
                            movie = movie,
                            downloadStatus = downloadManager.getDownloadStatus(movie.id),
                            onMovieClick = { onMovieClick(movie) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieDownloadItem(
    movie: Movie,
    progress: Float = 0f,
    isDownloaded: Boolean = false,
    downloadStatus: String? = null,
    onMovieClick: (Movie) -> Unit,
    onCancelClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMovieClick(movie) },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F1F1F)
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Movie thumbnail
            AsyncImage(
                model = movie.thumbnailUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 120.dp, height = 70.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Movie info and progress
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "${movie.duration} • ${movie.category}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                
                if (!isDownloaded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Cancel/Delete button
            IconButton(onClick = onCancelClick) {
                Icon(
                    if (isDownloaded) Icons.Default.Delete else Icons.Default.Close,
                    contentDescription = if (isDownloaded) "Remove download" else "Cancel download",
                    tint = Color.White
                )
            }
        }
    }
}
