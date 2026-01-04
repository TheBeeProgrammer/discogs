package com.clara.domain.model

data class Artist(
    val id: Int,
    val name: String,
    val imageUrl: String? = null,
)