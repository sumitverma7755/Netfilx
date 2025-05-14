package com.example.netfix.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedDownloadButton(
    isDownloading: Boolean,
    isDownloaded: Boolean,
    progress: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (isDownloading) progress else 0f,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    Box(modifier = modifier) {
        OutlinedButton(
            onClick = onClick,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isDownloading) {
                    Box(
                        modifier = Modifier.size(24.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp,
                            strokeCap = StrokeCap.Round
                        )
                        Icon(
                            Icons.Default.Download,
                            contentDescription = "Downloading",
                            tint = Color.White,
                            modifier = Modifier
                                .size(16.dp)
                                .align(Alignment.Center)
                        )
                    }
                } else {
                    Icon(
                        if (isDownloaded) Icons.Default.Delete else Icons.Default.Download,
                        contentDescription = if (isDownloaded) "Remove Download" else "Download",
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = when {
                        isDownloading -> "Downloading"
                        isDownloaded -> "Remove"
                        else -> "Download"
                    }
                )
            }
        }
    }
}

@Composable
private fun CircularProgressWithIcon(
    progress: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.width * 0.1f
        drawArc(
            color = Color.White,
            startAngle = 0f,
            sweepAngle = 360f * progress,
            useCenter = false,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            ),
            size = Size(
                width = size.width - strokeWidth,
                height = size.height - strokeWidth
            )
        )
    }
}