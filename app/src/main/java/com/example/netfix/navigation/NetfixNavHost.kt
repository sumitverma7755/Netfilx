package com.example.netfix.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.netfix.navigation.BottomNavItem
import com.example.netfix.screens.*
import com.example.netfix.video.VideoDownloadManager
import com.example.netfix.data.movies

/**
 * Navigation host for the main app content
 */
@Composable
fun NetfixNavHost(
    navController: NavHostController,
    onLogout: () -> Unit,
    onSettingsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    downloadManager: VideoDownloadManager
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen(
                onMovieClick = { movie ->
                    navController.navigate("movie_details/${movie.id}")
                }
            )
        }
        
        composable(BottomNavItem.Search.route) {
            SearchScreen(
                onMovieClick = { movie ->
                    navController.navigate("movie_details/${movie.id}")
                }
            )
        }
        
        composable(BottomNavItem.NewHot.route) {
            ComingSoonScreen()
        }
        
        composable(BottomNavItem.Downloads.route) {
            DownloadsScreen(
                downloadManager = downloadManager,
                onMovieClick = { movie ->
                    navController.navigate("movie_details/${movie.id}")
                }
            )
        }
        
        composable(BottomNavItem.Profile.route) {
            ProfileScreen(
                onLogout = onLogout,
                onSettingsClick = onSettingsClick
            )
        }
        
        // Movie details screen
        composable("movie_details/{movieId}") { backStackEntry ->
            val movieIdString = backStackEntry.arguments?.getString("movieId")
            if (movieIdString != null) {
                val movieId = movieIdString.toIntOrNull() ?: 1 // Default to 1 if conversion fails
                MovieDetailsScreen(
                    movieId = movieId,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    downloadManager = downloadManager,
                    onMovieClick = { movie ->
                        navController.navigate("movie_details/${movie.id}")
                    }
                )
            }
        }
        
        // Settings screen
        composable("settings") {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        // LM Studio connection screen
        composable("lm_studio_connection") {
            LMStudioConnectionScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        // Video player screen
        composable("video_player/{movieId}") { backStackEntry ->
            val movieIdString = backStackEntry.arguments?.getString("movieId")
            if (movieIdString != null) {
                val movieId = movieIdString.toIntOrNull() ?: 1
                val movie = com.example.netfix.data.movies.find { it.id == movieId } 
                    ?: com.example.netfix.data.movies.first()
                
                VideoPlayerScreen(
                    movie = movie,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    downloadManager = downloadManager
                )
            }
        }
    }
}
