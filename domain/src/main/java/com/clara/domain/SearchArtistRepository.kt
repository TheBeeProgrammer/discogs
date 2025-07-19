package com.clara.domain

import com.clara.domain.model.Artist
import com.clara.domain.model.PaginatedResult
import com.clara.domain.model.Pagination
import com.clara.domain.usecase.model.UseCaseResult
import kotlinx.coroutines.flow.Flow

interface SearchArtistRepository {
    suspend fun searchArtist(
        query: String,
        pagination: Pagination
    ): Flow<UseCaseResult<PaginatedResult<List<Artist>>>>
}