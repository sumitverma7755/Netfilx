package com.example.netfix.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedDownloadButton(
    isDownloading: Boolean,
    isDownloaded: Boolean,
    progress: Float = 0f,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(CircleShape)
    ) {
        OutlinedButton(
            onClick = onClick,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (isDownloaded) MaterialTheme.colorScheme.primary else Color.White
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isDownloading) {
                    CircularProgressIndicator(
                        progress = progress,
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeCap = StrokeCap.Round
                    )
                } else {
                    Icon(
                        if (isDownloaded) Icons.Default.CheckCircle else Icons.Default.Download,
                        contentDescription = if (isDownloaded) "Downloaded" else "Download",
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = when {
                        isDownloading -> "${(progress * 100).toInt()}%"
                        isDownloaded -> "Downloaded"
                        else -> "Download"
                    }
                )
            }
        }
    }
}