package com.clara.domain.model

data class Releases(
    val id: Int,
    val title: String,
    val releaseYear: String,
    val imageUrl: String? = null,
)
