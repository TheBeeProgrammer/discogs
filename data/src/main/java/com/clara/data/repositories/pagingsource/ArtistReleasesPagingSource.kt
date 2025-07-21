package com.clara.data.repositories.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.clara.data.api.ApiConstants
import com.clara.data.api.DiscogsApiService
import com.clara.data.api.PagingSourceConstants
import com.clara.data.mapper.ApiArtistReleaseResponseMapper
import com.clara.domain.model.InternalServerErrorException
import com.clara.domain.model.NetworkUnavailableException
import com.clara.domain.model.NotFoundException
import com.clara.domain.model.Releases
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
 * PagingSource for loading albums from the Discogs API.
 *
 * This class is responsible for fetching paginated album data for a specific artist.
 *
 * @param apiService The [com.clara.data.api.DiscogsApiService] used to make network requests.
 * @param mapper The [com.clara.data.mapper.ApiArtistReleaseResponseMapper] used to map API responses to domain models.
 * @param artistId The ID of the artist whose albums are being fetched.
 */
class ArtistReleasesPagingSource @Inject constructor(
    private val apiService: DiscogsApiService,
    private val mapper: ApiArtistReleaseResponseMapper,
    private val artistId: Int
) : PagingSource<Int, Releases>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Releases> {
        return try {
            val response = apiService.getArtistReleases(
                artistId = artistId,
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
            handleHttpErrorException(e)
        } catch (e: IOException) {
            handleIoException(e)
        } catch (e: Exception) {
            Logger.e(message = e.message.toString())
            LoadResult.Error(UnknownErrorException(e.message.toString()))
        }
    }

    /**
     * Handles [IOException] by mapping it to a specific [LoadResult.Error].
     *
     * This function logs the error message and returns a [LoadResult.Error] with a
     * corresponding domain-specific exception based on the type of [IOException].
     *
     * @param e The [IOException] that occurred.
     * @return A [LoadResult.Error] containing an appropriate domain-specific exception.
     */
    private fun handleIoException(e: IOException): LoadResult.Error<Int, Releases> {
        Logger.e(message = e.message.toString())
        return when (e) {
            is SocketTimeoutException -> LoadResult.Error(TimeoutException())
            is ConnectException -> LoadResult.Error(NetworkUnavailableException())
            else -> LoadResult.Error(UnknownErrorException(e.message.toString()))
        }
    }

    /**
     * Handles HTTP exceptions and maps them to appropriate [LoadResult.Error] types.
     *
     * This function logs the HTTP error and returns a [LoadResult.Error] containing
     * a specific exception type based on the HTTP status code.
     *
     * @param e The [HttpException] that occurred.
     * @return A [LoadResult.Error] containing an appropriate domain-specific exception.
     */
    private fun handleHttpErrorException(e: HttpException): LoadResult.Error<Int, Releases> {
        Logger.wtf(t = e, message = "HttpCode: ${e.code()}: " + e.message.toString())
        return when (e.code()) {
            ApiConstants.INTERNAL_SERVER_ERROR_CODE -> LoadResult.Error(
                InternalServerErrorException(
                    e.message()
                )
            )

            ApiConstants.NOT_FOUND_CODE -> LoadResult.Error(NotFoundException(e.message()))
            ApiConstants.UNAUTHORIZED_CODE -> LoadResult.Error(UnAuthorizedException(e.message()))
            else -> LoadResult.Error(UnknownErrorException(e.message()))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Releases>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}