package com.clara.data.common.mapper

import com.clara.data.remote.entities.ArtistReleasesResponse
import com.clara.domain.model.Album
import com.clara.domain.model.PaginatedResult
import javax.inject.Inject

/**
 * Maps [ArtistReleasesResponse] to [PaginatedResult] of [Album].
 */
class ApiArtistReleaseResponseMapper @Inject constructor() :
    Mapper<ArtistReleasesResponse, PaginatedResult<List<Album>>> {
    override fun map(from: ArtistReleasesResponse): PaginatedResult<List<Album>> {
        val albums = from.releases.map {
            Album(id = it.id.toString(), title = it.title, releaseYear = it.year.toString())
        }
        return PaginatedResult(
            data = albums,
            currentPage = from.pagination.page,
            totalPages = from.pagination.pages
        )
    }
}
