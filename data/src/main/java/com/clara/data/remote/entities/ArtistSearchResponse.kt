package com.clara.data.remote.entities

import com.google.gson.annotations.SerializedName

data class ArtistSearchResponse(
    @SerializedName("results") val results: List<ArtistResult>
)

data class ArtistResult(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val name: String,
    @SerializedName("thumb") val thumbnail: String?
)
