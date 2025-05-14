package com.example.netfix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.netfix.components.NetfixBottomNavigation
import com.example.netfix.data.SharedPreferencesAuth
import com.example.netfix.navigation.NetfixNavHost
import com.example.netfix.screens.LoginScreen
import com.example.netfix.screens.RegisterScreen
import com.example.netfix.ui.theme.NetfixTheme
import com.example.netfix.video.VideoDownloadManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun NetfixApp(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val auth = remember { SharedPreferencesAuth(context) }
    val downloadManager = remember { VideoDownloadManager(context) }
    
    // Check initial login state
    var isLoggedIn by remember { mutableStateOf(auth.isLoggedIn.value) }
    var showLogin by remember { mutableStateOf(true) }
    
    // Listen for auth state changes
    LaunchedEffect(Unit) {
        auth.isLoggedIn.collect { loginState ->
            isLoggedIn = loginState
        }
    }
    
    if (isLoggedIn) {
        // Main app content with bottom navigation
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NetfixBottomNavigation(navController = navController)
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                NetfixNavHost(
                    navController = navController,
                    onLogout = { auth.clearLoginState() },
                    onSettingsClick = {
                        navController.navigate("settings")
                    },
                    downloadManager = downloadManager,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    } else {
        // Authentication screens
        if (showLogin) {
            LoginScreen(
                onLoginSuccess = { isLoggedIn = true },
                onRegisterClick = { showLogin = false }
            )
        } else {
            RegisterScreen(
                onRegisterSuccess = { isLoggedIn = true },
                onLoginClick = { showLogin = true }
            )
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            NetfixTheme {
                NetfixApp()
            }
        }
    }
}