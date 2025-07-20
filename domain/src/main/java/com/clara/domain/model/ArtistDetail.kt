package com.clara.domain.model

data class ArtistDetail(
    val name: String,
    val profile: String?,
    val imageUrl: String?,
    val members: List<ArtistMembers>
)

data class ArtistMembers(val active: Boolean, val name: String)