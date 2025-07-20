package com.clara.data.api.entities

import com.google.gson.annotations.SerializedName

data class ApiPagination(
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("items") val items: Int,
    @SerializedName("per_page") val perPage: Int
)