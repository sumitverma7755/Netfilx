package com.example.netfix.model

data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val category: String = "",
    val duration: String = "",
    val year: Int = 0,
    val rating: Float = 0f
)
