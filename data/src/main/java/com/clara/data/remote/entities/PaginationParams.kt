package com.clara.data.remote.entities

import com.clara.data.remote.ApiConstants.DEFAULT_PAGE
import com.clara.data.remote.ApiConstants.PER_PAGE

data class PaginationParams(
    val page: Int = DEFAULT_PAGE,
    val perPage: Int = PER_PAGE
)