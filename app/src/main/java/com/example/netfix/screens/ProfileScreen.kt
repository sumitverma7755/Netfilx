package com.example.netfix.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.netfix.R
import com.example.netfix.data.SharedPreferencesAuth
import com.example.netfix.model.User
import com.example.netfix.model.UserDatabase
import com.example.netfix.model.UserProfile
import kotlinx.coroutines.flow.collect

/**
 * Profile screen for user account management
 */
@Composable
fun ProfileScreen(
    onLogout: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val auth = remember { SharedPreferencesAuth(context) }
    
    // Get current user
    var userId by remember { mutableStateOf("") }
    var user by remember { mutableStateOf<User?>(null) }
    
    // Collect user ID from auth
    LaunchedEffect(Unit) {
        auth.userId.collect { id ->
            userId = id
            if (id.isNotEmpty()) {
                user = UserDatabase.getUserById(id)
            }
        }
    }
    
    // Default selected profile
    val defaultProfile = UserProfile(
        id = "user1",
        name = "Main",
        preferredGenres = listOf("Action", "Drama")
    )
    var selectedProfile by remember { mutableStateOf(defaultProfile) }
    
    // Get profiles from user or use sample profiles if user is null
    val profiles = remember(user) {
        user?.profiles ?: listOf(
            defaultProfile,
            UserProfile(
                id = "user2",
                name = "Jamie",
                preferredGenres = listOf("Comedy", "Action")
            ),
            UserProfile(
                id = "user3",
                name = "Kids",
                preferredGenres = listOf("Animation", "Family")
            ),
            UserProfile(
                id = "add_profile",
                name = "Add Profile",
                preferredGenres = emptyList()
            )
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Profile & Settings",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Profile selection
        Text(
            text = "Who's watching?",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            items(profiles) { profile ->
                ProfileItem(
                    profile = profile,
                    isSelected = profile.id == selectedProfile.id,
                    onClick = {
                        if (profile.id != "add_profile") {
                            selectedProfile = profile
                        }
                    }
                )
            }
        }
        
        // Settings
        LazyColumn {
            item {
                SettingsHeader(title = "Account")
                SettingsItem(
                    icon = Icons.Default.Person,
                    title = "Manage Profiles",
                    onClick = { /* Navigate to profile management */ }
                )
                SettingsItem(
                    icon = Icons.Default.Settings,
                    title = "App Settings",
                    onClick = onSettingsClick
                )
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "Subscription & Billing",
                    onClick = { /* Navigate to billing */ }
                )
                
                Divider(
                    color = Color.DarkGray,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                
                SettingsHeader(title = "Preferences")
                SettingsItem(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    onClick = { /* Navigate to notifications */ }
                )
                SettingsItem(
                    icon = Icons.Default.List,
                    title = "Download Settings",
                    onClick = { /* Navigate to download settings */ }
                )
                SettingsItem(
                    icon = Icons.Default.PlayArrow,
                    title = "Playback Settings",
                    onClick = { /* Navigate to playback settings */ }
                )
                
                Divider(
                    color = Color.DarkGray,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                
                SettingsItem(
                    icon = Icons.Default.ArrowForward,
                    title = "Sign Out",
                    onClick = { 
                        // Sign out logic
                        auth.clearLoginState()
                        onLogout()
                    },
                    tint = colorResource(id = R.color.netflix_red)
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = "App version: 1.0.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp)
                )
                
                // Show user email if available
                if (user != null) {
                    Text(
                        text = "Logged in as: ${user?.email}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileItem(
    profile: UserProfile,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .clickable(onClick = onClick)
    ) {
        if (profile.id == "add_profile") {
            // Add profile button
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray)
                    .border(
                        width = if (isSelected) 2.dp else 0.dp,
                        color = colorResource(id = R.color.netflix_red),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Profile",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        } else {
            // Profile avatar
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(getProfileColor(profile.id))
                    .border(
                        width = if (isSelected) 2.dp else 0.dp,
                        color = colorResource(id = R.color.netflix_red),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = profile.name.first().toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = profile.name,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}

@Composable
fun SettingsHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    tint: Color = Color.White
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Icon(
            icon,
            contentDescription = title,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = tint
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Icon(
            Icons.Default.ArrowForward,
            contentDescription = "Navigate",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

// Helper function to generate profile colors
private fun getProfileColor(userId: String): Color {
    return when (userId) {
        "user1" -> Color(0xFF1E88E5) // Blue
        "user2" -> Color(0xFFD81B60) // Pink
        "user3" -> Color(0xFF43A047) // Green
        else -> Color(0xFF6D4C41)    // Brown
    }
}
