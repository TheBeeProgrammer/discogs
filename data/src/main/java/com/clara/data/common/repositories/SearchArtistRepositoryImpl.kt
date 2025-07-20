package com.clara.data.common.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.clara.data.remote.ArtistPagingSource
import com.clara.data.remote.ApiConstants
import com.clara.data.remote.DiscogsApiService
import com.clara.data.common.mapper.ApiArtistSearchResponseMapper
import com.clara.domain.SearchArtistRepository
import com.clara.domain.model.Artist
import com.clara.domain.usecase.model.UseCaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of [SearchArtistRepository] that fetches artist search results
 * from the Discogs API and provides them as a paginated flow.
 *
 * This class handles the interaction with the [DiscogsApiService] for network requests
 * and uses an [ApiArtistSearchResponseMapper] to transform the API response into
 * domain models. It leverages the Paging 3 library to provide efficient data loading.
 *
 * @param apiService The service responsible for making API calls to Discogs.
 * @param mapper The mapper responsible for converting API responses to domain models.
 */
class SearchArtistRepositoryImpl @Inject constructor(
    private val apiService: DiscogsApiService,
    private val mapper: ApiArtistSearchResponseMapper
) : SearchArtistRepository, BaseRepository() {

    /**
     * Searches for artists based on a query string.
     *
     * This function utilizes the Paging library to handle pagination of search results.
     * It makes a network request to the Discogs API to fetch artists matching the query.
     * The results are then mapped to the domain [Artist] model.
     *
     * @param query The search string to use for finding artists.
     * @return A [UseCaseResult] containing a [Flow] of [PagingData] for [Artist] objects.
     *         This can be either a [UseCaseResult.Success] with the data flow or a
     *         [UseCaseResult.Error] if an exception occurred during the operation.
     */
    override suspend fun searchArtist(query: String): UseCaseResult<Flow<PagingData<Artist>>> {
        return safeCall {
            val pagingSource = ArtistPagingSource(apiService, mapper, query)
            Pager(
                config = PagingConfig(pageSize = ApiConstants.PER_PAGE),
                pagingSourceFactory = { pagingSource }
            ).flow
        }
    }
}
