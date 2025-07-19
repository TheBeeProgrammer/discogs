package com.clara.domain.model

data class PaginatedResult<T>(
    val data: T,
    val currentPage: Int,
    val totalPages: Int
)
