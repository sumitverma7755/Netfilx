package com.example.netfix.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.netfix.model.Movie

@Composable
fun FeaturedMovieBanner(
    featuredMovie: Movie,
    onPlayClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
    ) {
        // Featured movie image
        AsyncImage(
            model = featuredMovie.thumbnailUrl,
            contentDescription = featuredMovie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        // Gradient overlay for better text visibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.9f)
                        )
                    )
                )
        )
        
        // Content overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = featuredMovie.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Year and category
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${featuredMovie.year}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "•",
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = featuredMovie.category,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Description
            Text(
                text = featuredMovie.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                maxLines = 3
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Play button
            Button(
                onClick = onPlayClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .height(48.dp)
                    .clip(RoundedCornerShape(4.dp))
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "Play",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
