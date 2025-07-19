package com.clara.data.remote.entities

import com.google.gson.annotations.SerializedName

data class ApiArtistSearchResponse(
    @SerializedName("pagination") val pagination: ApiPagination,
    @SerializedName("results") val results: List<ApiArtist>
)

data class ApiArtist(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("thumb") val thumb: String?,
    @SerializedName("resource_url") val resourceUrl: String?
)