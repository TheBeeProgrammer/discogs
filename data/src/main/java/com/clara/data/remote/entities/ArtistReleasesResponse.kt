package com.clara.data.remote.entities

import com.google.gson.annotations.SerializedName

data class ArtistReleasesResponse(
    @SerializedName("pagination") val pagination: ApiPagination,
    @SerializedName("releases") val releases: List<ArtistRelease>
)

data class ArtistRelease(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("year") val year: Int?,
    @SerializedName("label") val label: String?,
    @SerializedName("thumb") val thumbnail: String?,
    @SerializedName("type") val type: String?
)
