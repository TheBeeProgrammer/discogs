package com.clara.data.remote.entities

import com.google.gson.annotations.SerializedName

data class ReleaseDetailResponse(
    val id: Int,
    val title: String,
    @SerializedName("year") val releaseYear: Int?,
    val genres: List<String>?,
    val styles: List<String>?,
    val labels: List<Label>?,
    val tracklist: List<Track>?,
    val images: List<Image>?,
    val artists: List<ArtistShort>?
)

data class Label(
    val name: String
)

data class Track(
    val position: String?,
    val title: String,
    val duration: String?
)

data class Image(
    val type: String?,
    val uri: String?,
    @SerializedName("uri150") val thumbnail: String?
)

data class ArtistShort(
    val id: Int,
    val name: String
)