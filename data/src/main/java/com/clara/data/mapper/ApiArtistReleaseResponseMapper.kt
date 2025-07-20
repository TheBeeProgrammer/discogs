package com.clara.data.mapper

import com.clara.data.api.entities.ArtistReleasesResponse
import com.clara.domain.model.Releases
import com.clara.domain.model.PaginatedResult
import javax.inject.Inject

/**
 * Maps [ArtistReleasesResponse] to [PaginatedResult] of [Releases].
 */
class ApiArtistReleaseResponseMapper @Inject constructor() :
    Mapper<ArtistReleasesResponse, PaginatedResult<List<Releases>>> {
    override fun map(from: ArtistReleasesResponse): PaginatedResult<List<Releases>> {
        val releases = from.releases.map {
            Releases(
                id = it.id.toString(),
                title = it.title,
                releaseYear = it.year.toString(),
                imageUrl = it.thumbnail
            )
        }
        return PaginatedResult(
            data = releases,
            currentPage = from.pagination.page,
            totalPages = from.pagination.pages
        )
    }
}
