package com.example.netfix.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.netfix.repository.LMStudioRepository
import com.example.netfix.util.LMStudioConnection
import kotlinx.coroutines.launch

@Composable
fun LMStudioConnectionScreen(
    onBackClick: () -> Unit
) {
    var ipAddress by remember { mutableStateOf("") }
    var connectionStatus by remember { mutableStateOf<ConnectionStatus>(ConnectionStatus.NotTested) }
    var isLoading by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    val repository = remember { LMStudioRepository() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Header with back button
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
                text = "LM Studio Connection",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            // Empty spacer for alignment
            Spacer(modifier = Modifier.width(48.dp))
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // IP Address input
        OutlinedTextField(
            value = ipAddress,
            onValueChange = { ipAddress = it },
            label = { Text("Computer IP Address") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.DarkGray,
                unfocusedContainerColor = Color.DarkGray,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.LightGray
            ),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Test connection button
        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    connectionStatus = ConnectionStatus.Testing
                    
                    // First test basic connection
                    val isReachable = LMStudioConnection.testConnection(ipAddress)
                    if (!isReachable) {
                        connectionStatus = ConnectionStatus.Failed("Server not reachable")
                        isLoading = false
                        return@launch
                    }
                    
                    // If reachable, configure and test API
                    try {
                        LMStudioConnection.configure(ipAddress)
                        val response = repository.sendMessage("test")
                        connectionStatus = if (response.isSuccessful) {
                            ConnectionStatus.Success
                        } else {
                            ConnectionStatus.Failed("API test failed: ${response.code()}")
                        }
                    } catch (e: Exception) {
                        connectionStatus = ConnectionStatus.Failed(e.message ?: "Unknown error")
                    }
                    
                    isLoading = false
                }
            },
            enabled = !isLoading && ipAddress.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE50914) // Netflix red
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Test Connection")
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Status display
        when (val status = connectionStatus) {
            ConnectionStatus.NotTested -> {
                Text(
                    text = "Enter your computer's IP address and test the connection",
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            ConnectionStatus.Testing -> {
                Text(
                    text = "Testing connection...",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            ConnectionStatus.Success -> {
                Text(
                    text = "✅ Connected successfully!",
                    color = Color.Green,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is ConnectionStatus.Failed -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "❌ Connection failed",
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = status.error,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Make sure:\n" +
                              "1. LM Studio is running\n" +
                              "2. Server is enabled in LM Studio\n" +
                              "3. Your computer and phone are on the same network\n" +
                              "4. The IP address is correct\n" +
                              "5. No firewall is blocking the connection",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

sealed class ConnectionStatus {
    object NotTested : ConnectionStatus()
    object Testing : ConnectionStatus()
    object Success : ConnectionStatus()
    data class Failed(val error: String) : ConnectionStatus()
}
