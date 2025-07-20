package com.clara.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.clara.data.common.mapper.ApiArtistReleaseResponseMapper
import com.clara.data.remote.ApiConstants.FORBIDDEN_CODE
import com.clara.data.remote.ApiConstants.UNAUTHORIZED_CODE
import com.clara.data.remote.PagingSourceConstants.FIRST_PAGE_NUMBER
import com.clara.data.remote.PagingSourceConstants.MINIMAL_PAGE_NUMBER
import com.clara.domain.model.Releases
import com.clara.domain.model.ForbiddenException
import com.clara.domain.model.NetworkUnavailableException
import com.clara.domain.model.UnauthorizedException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * PagingSource for loading releases from the Discogs API.
 *
 * This class is responsible for fetching paginated album data for a specific artist.
 *
 * @param apiService The [DiscogsApiService] used to make network requests.
 * @param mapper The [ApiArtistReleaseResponseMapper] used to map API responses to domain models.
 * @param artistId The ID of the artist whose releases are being fetched.
 */
class ReleasePagingSource @Inject constructor(
    private val apiService: DiscogsApiService,
    private val mapper: ApiArtistReleaseResponseMapper,
    private val artistId: Int
) : PagingSource<Int, Releases>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Releases> {
        return try {
            val response = apiService.getArtistReleases(
                artistId = artistId,
                page = params.key ?: FIRST_PAGE_NUMBER,
                perPage = params.loadSize
            )

            val paginatedResult = mapper.map(response)
            LoadResult.Page(
                data = paginatedResult.data,
                prevKey = paginatedResult.currentPage.dec().takeIf { it > MINIMAL_PAGE_NUMBER },
                nextKey = (paginatedResult.currentPage + FIRST_PAGE_NUMBER).takeIf {
                    it <= paginatedResult.totalPages
                }
            )
        } catch (e: HttpException) {
            when (e.code()) {
                UNAUTHORIZED_CODE -> LoadResult.Error(UnauthorizedException(e.message()))
                FORBIDDEN_CODE -> LoadResult.Error(ForbiddenException(e.message()))
                else -> LoadResult.Error(e)
            }
        } catch (_: IOException) {
            LoadResult.Error(NetworkUnavailableException())
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Releases>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}