package com.clara.domain.repositories

import com.clara.domain.model.ArtistDetail
import com.clara.domain.usecase.model.UseCaseResult
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for retrieving artist details.
 */
interface ArtistDetailRepository {
    suspend fun getArtistDetail(artistId: Int): UseCaseResult<Flow<ArtistDetail>>
}