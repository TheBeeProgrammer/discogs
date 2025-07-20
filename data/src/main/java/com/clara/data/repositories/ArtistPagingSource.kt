package com.clara.data.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.clara.data.api.ApiConstants
import com.clara.data.api.DiscogsApiService
import com.clara.data.api.PagingSourceConstants
import com.clara.data.mapper.ApiArtistSearchResponseMapper
import com.clara.domain.model.Artist
import com.clara.domain.model.InternalServerErrorException
import com.clara.domain.model.NetworkUnavailableException
import com.clara.domain.model.UnknownErrorException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * PagingSource for fetching artists from the Discogs API based on a search query.
 *
 * This class handles the logic of loading paginated data from the API, mapping the API response
 * to domain models, and providing the necessary information for the Paging library to display
 * the data in a list.
 *
 * @param apiService The [com.clara.data.api.DiscogsApiService] instance used to make network requests.
 * @param mapper The [ApiArtistSearchResponseMapper] instance used to map API responses to domain models.
 * @param query The search query string used to filter artists.
 */
class ArtistPagingSource @Inject constructor(
    private val apiService: DiscogsApiService,
    private val mapper: ApiArtistSearchResponseMapper,
    private val query: String
) : PagingSource<Int, Artist>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {
        return try {
            val response = apiService.searchArtists(
                query = query,
                page = params.key ?: PagingSourceConstants.FIRST_PAGE_NUMBER,
                perPage = params.loadSize
            )

            val paginatedResult = mapper.map(response)

            LoadResult.Page(
                data = paginatedResult.data,
                prevKey = paginatedResult.currentPage.dec()
                    .takeIf { it > PagingSourceConstants.MINIMAL_PAGE_NUMBER },
                nextKey = (paginatedResult.currentPage + PagingSourceConstants.FIRST_PAGE_NUMBER).takeIf {
                    it <= paginatedResult.totalPages
                }
            )
        } catch (e: HttpException) {
            when (e.code()) {
                ApiConstants.INTERNAL_SERVER_ERROR -> LoadResult.Error(
                    InternalServerErrorException(
                        e.message()
                    )
                )

                else -> LoadResult.Error(UnknownErrorException(e.message ?: ""))
            }
        } catch (_: IOException) {
            LoadResult.Error(NetworkUnavailableException())
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}