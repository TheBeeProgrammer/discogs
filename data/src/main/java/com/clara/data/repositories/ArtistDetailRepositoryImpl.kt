package com.clara.data.repositories

import com.clara.data.mapper.ApiArtistDetailResponseMapper
import com.clara.data.api.DiscogsApiService
import com.clara.domain.model.ArtistDetail
import com.clara.domain.repositories.ArtistDetailRepository
import com.clara.domain.usecase.base.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Implementation of [ArtistDetailRepository] that fetches artist details from the Discogs API.
 *
 * @property apiService The [DiscogsApiService] used to make network requests.
 * @property mapper The [ApiArtistDetailResponseMapper] used to map the API response to a domain model.
 */
class ArtistDetailRepositoryImpl @Inject constructor(
    private val apiService: DiscogsApiService,
    private val mapper: ApiArtistDetailResponseMapper,
) : ArtistDetailRepository, BaseRepository() {
    /**
     * Retrieves the details of an artist with the given ID.
     *
     * @param artistId The ID of the artist to retrieve details for.
     * @return A [UseCaseResult] containing a [Flow] of [ArtistDetail] if successful, or an error otherwise.
     */
    override suspend fun getArtistDetail(
        artistId: Int
    ): UseCaseResult<Flow<ArtistDetail>> = safeCall {
        val response = apiService.getArtistDetails(artistId)
        val artist = mapper.map(response)
        flowOf(artist)
    }
}