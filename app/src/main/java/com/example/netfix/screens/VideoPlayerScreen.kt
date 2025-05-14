package com.example.netfix.screens

import android.app.PictureInPictureParams
import android.content.pm.PackageManager
import android.os.Build
import android.util.Rational
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.netfix.model.Movie
import com.example.netfix.video.CachedMediaSourceFactory
import com.example.netfix.data.WatchHistoryManager
import com.example.netfix.data.DownloadPreferences
import com.example.netfix.video.VideoDownloadManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Video player screen using Media3 with PiP support
 */
@UnstableApi
@Composable
fun VideoPlayerScreen(
    movie: Movie,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val downloadPreferences = remember { DownloadPreferences(context) }
    val downloadManager = remember { VideoDownloadManager(context) }
    val watchHistoryManager = remember {
        WatchHistoryManager(context, downloadPreferences, downloadManager)
    }
    val coroutineScope = rememberCoroutineScope()
    val mediaSourceFactory = remember { CachedMediaSourceFactory(context) }

    var showControls by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isBuffering by remember { mutableStateOf(false) }
    var isWatched by remember { mutableStateOf(false) }

    // Check if movie has been watched before
    LaunchedEffect(movie.id) {
        watchHistoryManager.getLastWatchTime(movie.id).collect { timestamp ->
            isWatched = timestamp != null
        }
    }

    // Track when video is watched
    val onVideoCompleted = {
        coroutineScope.launch {
            watchHistoryManager.markAsWatched(movie.id)
        }
    }

    // Auto-hide controls after a delay
    LaunchedEffect(showControls) {
        if (showControls) {
            delay(5000) // 5 seconds
            showControls = false
        }
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            try {
                setMediaItem(MediaItem.fromUri(movie.videoUrl))
                prepare()
                playWhenReady = isPlaying

                // Add a listener to handle player events
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(state: Int) {
                        when (state) {
                            Player.STATE_BUFFERING -> {
                                isBuffering = true
                            }
                            Player.STATE_READY -> {
                                isBuffering = false
                            }
                            Player.STATE_ENDED -> {
                                isPlaying = false
                                onVideoCompleted()
                            }
                            Player.STATE_IDLE -> {
                                // Error state
                                errorMessage = "Unable to play video"
                            }
                        }
                    }

                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        errorMessage = error.message
                    }
                })
            } catch (e: Exception) {
                errorMessage = "Error loading video: ${e.message}"
            }
        }
    }

    // Handle PiP mode
    val supportsPip = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
        } else false
    }

    // Setup PiP params
    val params = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PictureInPictureParams.Builder()
                .setAspectRatio(Rational(16, 9))
                .build()
        } else null
    }

    // Lifecycle handling
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    if (supportsPip && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        val activity = context as? android.app.Activity
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            activity?.enterPictureInPictureMode(params!!)
                        } else {
                            activity?.enterPictureInPictureMode()
                        }
                    }
                }
                Lifecycle.Event.ON_STOP -> {
                    exoPlayer.pause()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    exoPlayer.release()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Handle back press
    BackHandler {
        exoPlayer.release()
        onBackClick()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { showControls = !showControls }
    ) {
        // Video player
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    useController = false
                    setShutterBackgroundColor(android.graphics.Color.BLACK)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Loading indicator
        if (isBuffering) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.Red
                )
            }
        }

        // Error message
        errorMessage?.let { error ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.8f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Playback Error",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onBackClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE50914) // Netflix red
                            )
                        ) {
                            Text("Go Back")
                        }
                    }
                }
            }
        }

        // Custom controls overlay
        if (showControls) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Back button
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                // Play/Pause button
                IconButton(
                    onClick = {
                        isPlaying = !isPlaying
                        exoPlayer.playWhenReady = isPlaying
                    },
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Icon(
                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }

                // Title and info
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}
