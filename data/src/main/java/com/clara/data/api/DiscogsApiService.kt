package com.clara.data.api

import com.clara.data.api.entities.ApiArtistSearchResponse
import com.clara.data.api.entities.ArtistDetailResponse
import com.clara.data.api.entities.ArtistReleasesResponse
import com.clara.data.api.entities.ReleaseDetailResponse
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
     * @param perPage The number of results per page. Defaults to [ApiConstants.PER_PAGE].
     * @param type The type of search to perform. Defaults to [DEFAULT_TYPE] (artist).
     * @return An [ApiArtistSearchResponse] with the search results.
     */
    @GET("database/search")
    suspend fun searchArtists(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = ApiConstants.PER_PAGE,
        @Query("type") type: String = DEFAULT_TYPE
    ): ApiArtistSearchResponse

    /**
     * Retrieves a list of releases by a specific artist.
     *
     * @param artistId The ID of the artist.
     * @param sort The field to sort the results by. Defaults to [DEFAULT_SORT] (year).
     *             Possible values include: "year", "title", "format".
     * @param order The order to sort the results in. Defaults to [DEFAULT_SORT_ORDER] (descending).
     *              Possible values include: "asc", "desc".
     * @param page The page number of the results to retrieve. Defaults to [ApiConstants.DEFAULT_PAGE].
     * @param perPage The number of results per page. Defaults to [ApiConstants.PER_PAGE].
     * @return An [ArtistReleasesResponse] containing the list of releases.
     */
    @GET("artists/{artist_id}/releases")
    suspend fun getArtistReleases(
        @Path("artist_id") artistId: Int,
        @Query("sort") sort: String = DEFAULT_SORT,
        @Query("sort_order") order: String = DEFAULT_SORT_ORDER,
        @Query("page") page: Int = ApiConstants.DEFAULT_PAGE,
        @Query("per_page") perPage: Int = ApiConstants.PER_PAGE
    ): ArtistReleasesResponse

    /**
     * Retrieves detailed information about a specific artist.
     *
     * @param artistId The ID of the artist.
     * @return An [ArtistDetailResponse] containing the artist's details.
     */
    @GET("artists/{artist_id}")
    suspend fun getArtistDetails(
        @Path("artist_id") artistId: Int
    ): ArtistDetailResponse

    /**
     * Retrieves detailed information about a specific release.
     *
     * @param releaseId The ID of the release.
     * @return A [ReleaseDetailResponse] containing the detailed information about the release.
     */
    @GET("releases/{release_id}")
    suspend fun getReleaseDetails(
        @Path("release_id") releaseId: Int
    ): ReleaseDetailResponse
}