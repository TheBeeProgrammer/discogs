package com.clara.domain.usecase.model

import com.clara.domain.model.Pagination

data class SearchArtistParams(
    val query: String,
    val pagination: Pagination
)
