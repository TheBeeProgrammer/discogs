package com.clara.data.mapper

import com.clara.data.api.entities.ApiArtistSearchResponse
import com.clara.domain.model.Artist
import com.clara.domain.model.PaginatedResult
import javax.inject.Inject

/**
 * Maps an [ApiArtistSearchResponse] to a [PaginatedResult] of [Artist]s.
 *
 * This mapper transforms the raw API response for an artist search into a more usable
 * domain model, extracting relevant artist information and pagination details.
 */
class ApiArtistSearchResponseMapper @Inject constructor() :
    Mapper<ApiArtistSearchResponse, PaginatedResult<List<Artist>>> {

    /**
     * Maps an [ApiArtistSearchResponse] to a [PaginatedResult] of [Artist] objects.
     *
     * @param from The [ApiArtistSearchResponse] to map.
     * @return A [PaginatedResult] containing a list of [Artist] objects and pagination information.
     */
    override fun map(from: ApiArtistSearchResponse): PaginatedResult<List<Artist>> {
        val artists = from.results.map {
            Artist(
                id = it.id.toString(),
                name = it.title,
                imageUrl = it.thumb.takeIf { thumb -> !thumb.isNullOrBlank() }
            )
        }

        return PaginatedResult(
            data = artists,
            currentPage = from.pagination.page,
            totalPages = from.pagination.pages
        )
    }
}