package com.clara.data.common.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.clara.data.common.mapper.ApiArtistReleaseResponseMapper
import com.clara.data.remote.ApiConstants
import com.clara.data.remote.DiscogsApiService
import com.clara.data.remote.ReleasePagingSource
import com.clara.domain.model.Releases
import com.clara.domain.repositories.ArtistReleasesRepository
import com.clara.domain.usecase.model.UseCaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of [ArtistReleasesRepository] that fetches artist releases data
 * from the Discogs API.
 *
 * @property apiService The [DiscogsApiService] instance for making API calls.
 * @property mapper The [ApiArtistReleaseResponseMapper] instance for mapping API responses to domain models.
 */
class ArtistReleasesRepositoryImpl @Inject constructor(
    private val apiService: DiscogsApiService,
    private val mapper: ApiArtistReleaseResponseMapper
) : ArtistReleasesRepository, BaseRepository() {
    /**
     * Fetches a paginated list of releases for a given artist.
     *
     * This function uses the [Pager] from the Paging library to handle pagination.
     * It creates an [ReleasePagingSource] to fetch data from the [DiscogsApiService]
     * and maps the API response to [Releases] domain models using the provided [mapper].
     *
     * @param artistId The ID of the artist for whom to fetch releases.
     * @return A [UseCaseResult] containing a [Flow] of [PagingData] of [Releases] objects.
     *         The result will be [UseCaseResult.Success] if the operation is successful,
     *         or [UseCaseResult.Error] if an error occurs.
     */
    override suspend fun getArtistReleases(artistId: Int): Flow<PagingData<Releases>> {
        val pagingSource = ReleasePagingSource(apiService, mapper, artistId)
        return Pager(
            config = PagingConfig(pageSize = ApiConstants.PER_PAGE),
            pagingSourceFactory = { pagingSource }
        ).flow
    }
}
