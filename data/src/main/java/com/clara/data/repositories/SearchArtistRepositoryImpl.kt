package com.clara.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.clara.data.api.ApiConstants
import com.clara.data.api.DiscogsApiService
import com.clara.data.mapper.ApiArtistSearchResponseMapper
import com.clara.data.repositories.pagingsource.ArtistPagingSource
import com.clara.domain.model.Artist
import com.clara.domain.repositories.SearchArtistRepository
import com.clara.domain.usecase.base.UseCaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of [SearchArtistRepository] that fetches artist search results
 * from the Discogs API and provides them as a paginated flow.
 *
 * This class handles the interaction with the [DiscogsApiService] for network requests
 * and uses an [ApiArtistSearchResponseMapper] to transform the API response into
 * domain models. It leverages the Paging 3 library to provide efficient data loading.
 * It extends [com.clara.data.repositories.base.BaseRepository] to handle API call safety and error management.
 *
 * @param apiService The service responsible for making API calls to Discogs.
 * @param mapper The mapper responsible for converting API responses to domain models.
 */
class SearchArtistRepositoryImpl @Inject constructor(
    private val apiService: DiscogsApiService,
    private val mapper: ApiArtistSearchResponseMapper
) : SearchArtistRepository {

    /**
     * Searches for artists based on a query string.
     *
     * This function utilizes the Paging library to handle pagination of search results.
     * It makes a network request to the Discogs API to fetch artists matching the query.
     * The results are then mapped to the domain [Artist] model via [com.clara.data.repositories.pagingsource.ArtistPagingSource].
     *
     * @param query The search string to use for finding artists.
     * @return A [Flow] of [PagingData] for [Artist] objects.
     */
    override suspend fun searchArtist(query: String): Flow<PagingData<Artist>> {
        val pagingSource = ArtistPagingSource(apiService, mapper, query)
        return Pager(
            config = PagingConfig(pageSize = ApiConstants.PER_PAGE, initialLoadSize = ApiConstants.PER_PAGE),
            pagingSourceFactory = { pagingSource }
        ).flow
    }
}
