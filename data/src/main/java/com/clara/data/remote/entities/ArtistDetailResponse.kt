package com.clara.data.remote.entities

import com.google.gson.annotations.SerializedName

data class ArtistDetailResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("profile") val profile: String?,
    @SerializedName("images") val images: List<DiscogsImage>?,
    @SerializedName("members") val members: List<ArtistMember>?
)

data class ArtistMember(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("active") val isActive: Boolean
)

data class DiscogsImage(
    @SerializedName("uri") val uri: String,
    @SerializedName("type") val type: String
)
