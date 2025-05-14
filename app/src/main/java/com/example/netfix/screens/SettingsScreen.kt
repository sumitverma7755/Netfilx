package com.example.netfix.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.netfix.video.MediaCacheConfig
import com.example.netfix.data.DownloadPreferences
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onLMStudioClick: () -> Unit = {}  // Add new parameter
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val downloadPrefs = remember { DownloadPreferences(context) }
    
    var downloadFolderSize by remember { mutableStateOf(0L) }
    var isCalculatingSize by remember { mutableStateOf(true) }
    var wifiOnlyDownloads by remember { mutableStateOf<Boolean>(true) }
    var autoDeleteWatched by remember { mutableStateOf<Boolean>(false) }
    
    // Collect preferences
    LaunchedEffect(Unit) {
        downloadPrefs.wifiOnlyDownloads.collect { wifiOnly ->
            wifiOnlyDownloads = wifiOnly
        }
    }
    
    LaunchedEffect(Unit) {
        downloadPrefs.autoDeleteWatched.collect { autoDelete ->
            autoDeleteWatched = autoDelete
        }
    }
    
    // Calculate download folder size
    LaunchedEffect(Unit) {
        scope.launch {
            val size = File(context.getExternalFilesDir(null), "netfix_downloads")
                .walkBottomUp()
                .filter { it.isFile }
                .map { it.length() }
                .sum()
            downloadFolderSize = size
            isCalculatingSize = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Back button in header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = "Download Settings",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            // Empty spacer for alignment
            Spacer(modifier = Modifier.width(48.dp))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Storage section
        Text(
            text = "Storage",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1F1F1F)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                if (isCalculatingSize) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(
                        text = "Downloads: ${formatSize(downloadFolderSize)}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = {
                        scope.launch {
                            MediaCacheConfig.clearCache(context)
                            downloadFolderSize = 0
                        }
                    },
                    enabled = downloadFolderSize > 0,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Clear Downloads")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Download preferences
        Text(
            text = "Download Preferences",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1F1F1F)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Wi-Fi Only",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                        Text(
                            text = "Download only when connected to Wi-Fi",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                    Switch(
                        checked = wifiOnlyDownloads,
                        onCheckedChange = { 
                            scope.launch {
                                downloadPrefs.setWifiOnly(it)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Auto Delete Watched",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                        Text(
                            text = "Remove downloads after watching",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                    Switch(
                        checked = autoDeleteWatched,
                        onCheckedChange = { 
                            scope.launch {
                                downloadPrefs.setAutoDelete(it)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // LM Studio section
        Text(
            text = "AI Integration",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1F1F1F)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLMStudioClick() },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "LM Studio Connection",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                        Text(
                            text = "Configure connection to LM Studio",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = "Configure LM Studio",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}

private fun formatSize(bytes: Long): String {
    val kb = bytes / 1024.0
    val mb = kb / 1024.0
    val gb = mb / 1024.0
    
    return when {
        gb >= 1.0 -> String.format("%.1f GB", gb)
        mb >= 1.0 -> String.format("%.1f MB", mb)
        kb >= 1.0 -> String.format("%.1f KB", kb)
        else -> "$bytes bytes"
    }
}