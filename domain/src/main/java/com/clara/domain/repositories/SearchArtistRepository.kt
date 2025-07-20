package com.clara.domain.repositories

import androidx.paging.PagingData
import com.clara.domain.model.Artist
import com.clara.domain.usecase.base.UseCaseResult
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for searching artists.
 */
interface SearchArtistRepository {
    suspend fun searchArtist(query: String): Flow<PagingData<Artist>>
}