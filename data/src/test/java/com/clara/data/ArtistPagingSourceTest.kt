package com.clara.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import com.clara.data.api.DiscogsApiService
import com.clara.data.api.entities.ApiArtist
import com.clara.data.api.entities.ApiArtistSearchResponse
import com.clara.data.api.entities.ApiPagination
import com.clara.data.mapper.ApiArtistSearchResponseMapper
import com.clara.data.repositories.pagingsource.ArtistPagingSource
import com.clara.domain.model.Artist
import com.clara.domain.model.InternalServerErrorException
import com.clara.domain.model.NetworkUnavailableException
import com.clara.domain.model.PaginatedResult
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ArtistPagingSourceTest {

    private val apiService = mock<DiscogsApiService>()
    private val mapper = mock<ApiArtistSearchResponseMapper>()
    private val query = "The Beatles"

    private lateinit var pagingSource: ArtistPagingSource

    @Before
    fun setup() {
        pagingSource = ArtistPagingSource(apiService, mapper, query)
    }

    @Test
    fun `load returns Page on successful load`() = runTest {
        val fakeResponse = fakeArtistSearchResponse()
        val fakeMappedData = PaginatedResult(
            data = listOf(fakeArtist()),
            currentPage = 1,
            totalPages = 10
        )

        whenever(
            apiService.searchArtists(
                query = eq(query),
                any(),
                any(),
                any(),
            )
        ).thenReturn(fakeResponse)

        whenever(mapper.map(fakeResponse)).thenReturn(fakeMappedData)

        val pagingSource = ArtistPagingSource(apiService, mapper, query)
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        assertThat(result).isInstanceOf(PagingSource.LoadResult.Page::class.java)
        val page = result as PagingSource.LoadResult.Page
        assertThat(page.data).hasSize(1)
        assertThat(page.prevKey).isNull()
        assertThat(page.nextKey).isEqualTo(2)
    }

    @Test
    fun `load returns Error on HttpException 500`() = runTest {
        val exception = HttpException(Response.error<Any>(500, "error".toResponseBody(null)))

        whenever(apiService.searchArtists(any(), any(), any(), any())).thenThrow(
            exception
        )

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        assertThat(result).isInstanceOf(PagingSource.LoadResult.Error::class.java)
        val error = result as PagingSource.LoadResult.Error
        assertThat(error.throwable).isInstanceOf(InternalServerErrorException::class.java)
    }


    @Test
    fun `load returns Error on IOException`() = runTest {
        whenever(
            apiService.searchArtists(
                any(),
                any(),
                any(),
                any()
            )
        ).thenAnswer { throw IOException("Network down") }

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        assertThat(result).isInstanceOf(PagingSource.LoadResult.Error::class.java)
        val error = result as PagingSource.LoadResult.Error
        assertThat(error.throwable).isInstanceOf(NetworkUnavailableException::class.java)
    }
}

fun fakeArtistSearchResponse(): ApiArtistSearchResponse {
    return ApiArtistSearchResponse(
        pagination = ApiPagination(
            items = 1,
            pages = 1,
            page = 1,
            perPage = 10
        ),
        results = listOf(
            ApiArtist(
                id = 1,
                title = "The Beatles",
                thumb = "https://example.com/thumb.jpg",
                resourceUrl = "https://example.com/resource"
            )
        )
    )
}

fun fakeArtist(): Artist {
    return Artist(
        id = "1",
        name = "The Beatles",
        imageUrl = "https://example.com/resource"
    )
}