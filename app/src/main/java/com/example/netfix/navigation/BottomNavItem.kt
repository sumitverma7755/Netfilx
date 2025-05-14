package com.example.netfix.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a bottom navigation item with its route, icon, and label
 */
sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : BottomNavItem(
        route = "home",
        icon = Icons.Default.Home,
        label = "Home"
    )
    
    object Search : BottomNavItem(
        route = "search",
        icon = Icons.Default.Search,
        label = "Search"
    )
    
    object NewHot : BottomNavItem(
        route = "new_hot",
        icon = Icons.Default.Notifications,
        label = "New & Hot"
    )
    
    object Downloads : BottomNavItem(
        route = "downloads",
        icon = Icons.Default.List,
        label = "Downloads"
    )
    
    object Profile : BottomNavItem(
        route = "profile",
        icon = Icons.Default.Person,
        label = "Profile"
    )
    
    companion object {
        val items = listOf(Home, Search, NewHot, Downloads, Profile)
    }
}
