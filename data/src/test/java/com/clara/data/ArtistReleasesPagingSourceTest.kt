package com.clara.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import com.clara.data.api.DiscogsApiService
import com.clara.data.api.entities.ApiPagination
import com.clara.data.api.entities.ArtistRelease
import com.clara.data.api.entities.ArtistReleasesResponse
import com.clara.data.mapper.ApiArtistReleaseResponseMapper
import com.clara.data.repositories.pagingsource.ArtistReleasesPagingSource
import com.clara.domain.model.InternalServerErrorException
import com.clara.domain.model.NetworkUnavailableException
import com.clara.domain.model.PaginatedResult
import com.clara.domain.model.Releases
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
class ArtistReleasesPagingSourceTest {

    private val apiService = mock<DiscogsApiService>()
    private val mapper = mock<ApiArtistReleaseResponseMapper>()
    private val artistId = 1234

    private lateinit var pagingSource: ArtistReleasesPagingSource

    @Before
    fun setup() {
        pagingSource = ArtistReleasesPagingSource(apiService, mapper, artistId)
    }

    @Test
    fun `load returns Page on successful load`() = runTest {
        val fakeResponse = fakeReleaseResponse()
        val fakeMappedData = PaginatedResult(
            data = listOf(fakeRelease()),
            currentPage = 1,
            totalPages = 10
        )

        whenever(
            apiService.getArtistReleases(
                artistId = eq(artistId),
                sort = any(),
                order = any(),
                page = any(),
                perPage = any()
            )
        ).thenReturn(fakeResponse)

        whenever(mapper.map(fakeResponse)).thenReturn(fakeMappedData)

        val pagingSource = ArtistReleasesPagingSource(apiService, mapper, artistId)
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

        whenever(apiService.getArtistReleases(any(), any(), any(), any(), any())).thenThrow(
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
            apiService.getArtistReleases(
                any(),
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

private fun fakeReleaseResponse(): ArtistReleasesResponse {
    return ArtistReleasesResponse(
        pagination = ApiPagination(
            items = 1,
            pages = 1,
            page = 1,
            perPage = 10
        ),
        releases = listOf(
            ArtistRelease(
                id = 1234,
                title = "Fake Album",
                year = 2000,
                label = "Fake Label",
                thumbnail = "https://example.com/thumb.jpg",
                type = "Album"
            )
        )
    )
}

private fun fakeRelease(): Releases {
    return Releases(
        id = "1234",
        title = "Fake Album",
        releaseYear = "2000",
        imageUrl = "https://example.com/thumb.jpg"
    )
}

