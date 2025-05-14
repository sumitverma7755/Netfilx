package com.example.netfix.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val NetflixColorScheme = darkColorScheme(
    primary = NetflixRed,
    onPrimary = Color.White,
    secondary = NetflixLightGray,
    onSecondary = Color.White,
    tertiary = NetflixGold,
    background = NetflixBlack,
    surface = NetflixDarkGray,
    onSurface = Color.White,
    error = NetflixError,
    onError = Color.White
)

@Composable
fun NetfixTheme(
    darkTheme: Boolean = true, // Always use dark theme for Netflix-like appearance
    content: @Composable () -> Unit
) {
    val colorScheme = NetflixColorScheme
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}