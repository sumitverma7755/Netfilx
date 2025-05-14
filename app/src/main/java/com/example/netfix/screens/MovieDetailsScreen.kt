package com.example.netfix.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.netfix.ai.SmartSearch
import com.example.netfix.components.MoviePoster
import com.example.netfix.components.AnimatedDownloadButton
import com.example.netfix.components.DownloadStatusIndicator
import com.example.netfix.data.movies
import com.example.netfix.model.Movie
import com.example.netfix.video.VideoDownloadManager
import com.example.netfix.video.VideoDownloadService
import com.example.netfix.viewmodel.MovieDetailsViewModel
import kotlinx.coroutines.launch

/**
 * Detailed view of a movie with recommendations and play options
 */
@Composable
fun MovieDetailsScreen(
    movieId: Int,
    viewModel: MovieDetailsViewModel = viewModel(),
    onBackClick: () -> Unit,
    onMovieClick: (Movie) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val downloadManager = remember { VideoDownloadManager(context) }
    val coroutineScope = rememberCoroutineScope()
    val downloadState by downloadManager.downloadState.collectAsState()
    val downloadProgress by downloadManager.downloadProgress.collectAsState()
    
    var isDownloaded by remember { mutableStateOf(false) }
    
    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
        downloadManager.getDownloadedMovies().collect { downloadedIds ->
            isDownloaded = movieId in downloadedIds
        }
    }

    // Show loading state
    if (uiState.isLoading) {
        LoadingScreen()
        return
    }

    val movie = uiState.movie ?: return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Hero section with backdrop image
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                ) {
                    // Movie backdrop
                    AsyncImage(
                        model = movie.thumbnailUrl,
                        contentDescription = movie.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.5f),
                                        Color.Black
                                    )
                                )
                            )
                    )
                    
                    // Back button
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    
                    // Content info at bottom
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = movie.title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${movie.year}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.LightGray
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = "•",
                                color = Color.LightGray
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = movie.duration,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.LightGray
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = "•",
                                color = Color.LightGray
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = movie.category,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.LightGray
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Rating
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color(0xFFFFD700), // Gold
                                modifier = Modifier.size(20.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(4.dp))
                            
                            Text(
                                text = "${movie.rating}/10",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            
            // Action buttons
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Play button
                    Button(
                        onClick = { onMovieClick(movie) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE50914) // Netflix red
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Play")
                        }
                    }
                    
                    // Download button with progress
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        val currentProgress = downloadProgress[movieId] ?: 0f
                        val isDownloading = downloadState is VideoDownloadManager.DownloadState.Downloading &&
                                (downloadState as VideoDownloadManager.DownloadState.Downloading).movieId == movieId
                        
                        AnimatedDownloadButton(
                            isDownloading = isDownloading,
                            isDownloaded = isDownloaded,
                            progress = currentProgress,
                            onClick = {
                                if (isDownloaded || isDownloading) {
                                    coroutineScope.launch {
                                        downloadManager.cancelDownload(movie)
                                    }
                                } else {
                                    coroutineScope.launch {
                                        downloadManager.downloadMovie(movie)
                                    }
                                }
                            }
                        )
                    }
                }
            }
            
            // Description
            item {
                Text(
                    text = movie.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // Similar Movies section
            if (uiState.similarMovies.isNotEmpty()) {
                item {
                    Text(
                        text = "Similar Movies",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                    
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.similarMovies) { movie ->
                            MoviePoster(
                                movie = movie,
                                onClick = { onMovieClick(movie) }
                            )
                        }
                    }
                }
            }
        }
        
        // Download status indicator overlay
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            if (downloadState !is VideoDownloadManager.DownloadState.Idle) {
                DownloadStatusIndicator(
                    downloadState = downloadState,
                    progress = downloadProgress[movieId] ?: 0f,
                    onRetry = {
                        coroutineScope.launch {
                            downloadManager.downloadMovie(movie)
                        }
                    },
                    onDismiss = {
                        coroutineScope.launch {
                            downloadManager.cancelDownload(movie)
                        }
                    }
                )
            }
        }
    }
}
