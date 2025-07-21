package com.clara.data.repositories.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.clara.data.api.ApiConstants
import com.clara.data.api.DiscogsApiService
import com.clara.data.api.PagingSourceConstants
import com.clara.data.mapper.ApiArtistReleaseResponseMapper
import com.clara.domain.model.InternalServerErrorException
import com.clara.domain.model.NetworkUnavailableException
import com.clara.domain.model.Releases
import com.clara.domain.model.UnknownErrorException
import com.clara.logger.Logger
import retrofit2.HttpException
import java.io.IOException
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
            Logger.e(message = "HttpCode: ${e.code()}: " + e.message.toString())
            when (e.code()) {
                ApiConstants.INTERNAL_SERVER_ERROR -> LoadResult.Error(
                    InternalServerErrorException(
                        e.message()
                    )
                )

                else -> LoadResult.Error(UnknownErrorException(e.message()))
            }
        } catch (e: IOException) {
            Logger.e(message = e.message.toString())
            LoadResult.Error(NetworkUnavailableException())
        } catch (e: Exception) {
            Logger.e(message = e.message.toString())
            LoadResult.Error(UnknownErrorException(e.message.toString()))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Releases>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}