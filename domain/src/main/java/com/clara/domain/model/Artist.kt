package com.clara.domain.model

data class Artist(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
)