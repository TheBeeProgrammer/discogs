package com.clara.data.remote

import com.clara.data.remote.ApiConstants.DEFAULT_PAGE
import com.clara.data.remote.ApiConstants.PER_PAGE
import com.clara.data.remote.DiscogsApiService.Companion.DEFAULT_SORT
import com.clara.data.remote.DiscogsApiService.Companion.DEFAULT_SORT_ORDER
import com.clara.data.remote.DiscogsApiService.Companion.DEFAULT_TYPE
import com.clara.data.remote.entities.ApiResult
import com.clara.data.remote.entities.ArtistDetailResponse
import com.clara.data.remote.entities.ArtistReleasesResponse
import com.clara.data.remote.entities.ArtistSearchResponse
import com.clara.data.remote.entities.ReleaseDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface for interacting with the Discogs API
 *
 * @see <a href="https://www.discogs.com/developers">Discogs API Documentation</a>
 */
interface DiscogsApiService {
    companion object {
        private const val DEFAULT_TYPE = "artist"
        private const val DEFAULT_SORT = "year"
        private const val DEFAULT_SORT_ORDER = "desc"
    }

    /**
     * Searches for artists based on a query string.
     *
     * @param query The search query string.
     * @param page The page number of the results to retrieve.
     * @param perPage The number of results per page. Defaults to [DEFAULT_PER_PAGE].
     * @param type The type of search to perform. Defaults to [DEFAULT_TYPE] (artist).
     * @return An [ApiResult] containing an [ArtistSearchResponse] with the search results.
     */
    @GET("database/search")
    suspend fun searchArtists(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = PER_PAGE,
        @Query("type") type: String = DEFAULT_TYPE
    ): ApiResult<ArtistSearchResponse>

    /**
     * Retrieves a list of releases by a specific artist.
     *
     * @param artistId The ID of the artist.
     * @param sort The field to sort the results by. Defaults to [DEFAULT_SORT] (year).
     * @param order The order to sort the results in. Defaults to [DEFAULT_SORT_ORDER] (descending).
     * @param page The page number of the results to retrieve. Defaults to [DEFAULT_PAGE].
     * @param perPage The number of results per page. Defaults to [DEFAULT_PER_PAGE].
     * @return An [ApiResult] containing an [ArtistReleasesResponse] with the list of releases.
     */
    @GET("artists/{artist_id}/releases")
    suspend fun getArtistReleases(
        @Path("artist_id") artistId: Int,
        @Query("sort") sort: String = DEFAULT_SORT,
        @Query("sort_order") order: String = DEFAULT_SORT_ORDER,
        @Query("page") page: Int = DEFAULT_PAGE,
        @Query("per_page") perPage: Int = PER_PAGE
    ): ApiResult<ArtistReleasesResponse>

    /**
     * Retrieves detailed information about a specific artist.
     *
     * @param artistId The ID of the artist.
     * @return An [ApiResult] containing the [ArtistDetailResponse] if successful, or an error otherwise.
     */
    @GET("artists/{artist_id}")
    suspend fun getArtistDetails(
        @Path("artist_id") artistId: Int
    ): ApiResult<ArtistDetailResponse>

    /**
     * Retrieves detailed information about a specific release.
     *
     * @param releaseId The ID of the release.
     * @return An [ApiResult] containing the [ReleaseDetailResponse] if successful, or an error otherwise.
     */
    @GET("releases/{release_id}")
    suspend fun getReleaseDetails(
        @Path("release_id") releaseId: Int
    ): ApiResult<ReleaseDetailResponse>
}