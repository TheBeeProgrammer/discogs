package com.clara.data.repositories.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.clara.data.api.ApiConstants
import com.clara.data.api.DiscogsApiService
import com.clara.data.api.PagingSourceConstants
import com.clara.data.mapper.ApiArtistSearchResponseMapper
import com.clara.domain.model.Artist
import com.clara.domain.model.InternalServerErrorException
import com.clara.domain.model.NetworkUnavailableException
import com.clara.domain.model.TimeoutException
import com.clara.domain.model.UnAuthorizedException
import com.clara.domain.model.UnknownErrorException
import com.clara.logger.Logger
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * PagingSource for fetching artists from the Discogs API based on a search query.
 *
 * This class handles the logic of loading paginated data from the API, mapping the API response
 * to domain models, and providing the necessary information for the Paging library to display
 * the data in a list.
 *
 * @param apiService The [com.clara.data.api.DiscogsApiService] instance used to make network requests.
 * @param mapper The [com.clara.data.mapper.ApiArtistSearchResponseMapper] instance used to map API responses to domain models.
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
            handleHttpError(e)
        } catch (e: IOException) {
            handleIoException(e)
        } catch (e: Exception) {
            Logger.e(message = e.message.toString())
            LoadResult.Error(UnknownErrorException(e.message ?: ""))
        }
    }

    /**
     * Handles IOExceptions that may occur during network requests.
     *
     * This function maps specific IOExceptions to corresponding domain-specific exceptions,
     * such as [TimeoutException] for [SocketTimeoutException] and [NetworkUnavailableException]
     * for [ConnectException]. For other IOExceptions, it defaults to [UnknownErrorException].
     *
     * @param e The [IOException] that occurred.
     * @return A [LoadResult.Error] containing the appropriate domain-specific exception.
     */
    private fun handleIoException(e: IOException): LoadResult.Error<Int, Artist> {
        Logger.e(message = e.message.toString())
        return when (e) {
            is SocketTimeoutException -> LoadResult.Error(TimeoutException())
            is NetworkUnavailableException -> LoadResult.Error(NetworkUnavailableException())
            else -> LoadResult.Error(UnknownErrorException(e.message.toString()))
        }
    }

    /**
     * Handles HTTP errors by mapping specific HTTP status codes to custom exceptions.
     *
     * This function takes an [HttpException] and returns a [LoadResult.Error] containing
     * a specific domain exception based on the HTTP status code.
     *
     * @param e The [HttpException] that occurred.
     * @return A [LoadResult.Error] containing a domain-specific exception.
     */
    private fun handleHttpError(e: HttpException): LoadResult.Error<Int, Artist> {
        Logger.wtf(t = e, message = "HttpCode: ${e.code()} " + e.message.toString())
        return when (e.code()) {
            ApiConstants.INTERNAL_SERVER_ERROR_CODE -> LoadResult.Error(
                InternalServerErrorException(
                    e.message()
                )
            )

            ApiConstants.UNAUTHORIZED_CODE -> LoadResult.Error(UnAuthorizedException(e.message()))
            else -> LoadResult.Error(UnknownErrorException(e.message ?: ""))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}