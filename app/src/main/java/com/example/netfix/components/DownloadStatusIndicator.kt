package com.example.netfix.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.netfix.video.VideoDownloadManager.DownloadState
import kotlinx.coroutines.delay

@Composable
fun DownloadStatusIndicator(
    downloadState: DownloadState,
    progress: Float = 0f,
    onRetry: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    var showIndicator by remember { mutableStateOf(true) }
    
    // Auto-hide success/error states after delay
    LaunchedEffect(downloadState) {
        if (downloadState is DownloadState.Error) {
            delay(5000)
            showIndicator = false
        }
    }
    
    AnimatedVisibility(
        visible = showIndicator,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = when (downloadState) {
                    is DownloadState.Error -> Color(0xFF421414)
                    is DownloadState.RequiresWifi -> Color(0xFF2D2D2D)
                    is DownloadState.NoNetwork -> Color(0xFF2D2D2D)
                    else -> Color(0xFF1F1F1F)
                }
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Status icon
                Icon(
                    imageVector = when (downloadState) {
                        is DownloadState.Downloading -> Icons.Default.Download
                        is DownloadState.Error -> Icons.Default.Error
                        is DownloadState.RequiresWifi -> Icons.Default.WifiOff
                        is DownloadState.NoNetwork -> Icons.Default.SignalWifiOff
                        else -> Icons.Default.Download
                    },
                    contentDescription = null,
                    tint = when (downloadState) {
                        is DownloadState.Error -> Color(0xFFE57373)
                        else -> Color.White
                    },
                    modifier = Modifier.size(24.dp)
                )
                
                // Message and progress
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = when (downloadState) {
                            is DownloadState.Downloading -> "Downloading..."
                            is DownloadState.Error -> "Download failed"
                            is DownloadState.RequiresWifi -> "Waiting for Wi-Fi"
                            is DownloadState.NoNetwork -> "No network connection"
                            else -> ""
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    
                    if (downloadState is DownloadState.Downloading) {
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    }
                }
                
                // Action button
                when (downloadState) {
                    is DownloadState.Error -> {
                        IconButton(onClick = onRetry) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Retry",
                                tint = Color.White
                            )
                        }
                    }
                    is DownloadState.RequiresWifi,
                    is DownloadState.NoNetwork -> {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Dismiss",
                                tint = Color.White
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}