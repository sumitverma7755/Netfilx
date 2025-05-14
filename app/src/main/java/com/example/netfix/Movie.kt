package com.example.netfix

data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnailUrl: String, // URL to the movie poster
    val videoUrl: String // URL to the video stream
)