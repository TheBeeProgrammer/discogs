package com.clara.domain.model

data class Album(
    val id: String,
    val title: String,
    val releaseYear: String,
    val imageUrl: String? = null,
)