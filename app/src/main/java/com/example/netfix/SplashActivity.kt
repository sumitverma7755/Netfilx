package com.example.netfix

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity

/**
 * Splash screen activity that displays the app logo
 * before transitioning to the main activity.
 */
class SplashActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Delay for 1.5 seconds to show the splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            // Start the main activity
            startActivity(Intent(this, MainActivity::class.java))
            
            // Close this activity
            finish()
        }, 1500)
    }
}
