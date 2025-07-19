package com.clara.data.remote.entities

import com.clara.data.common.mapper.Mapper
import com.clara.domain.model.Artist
import com.clara.domain.model.PaginatedResult
import javax.inject.Inject

class ApiArtistSearchResponseMapper @Inject constructor() :
    Mapper<ApiArtistSearchResponse, PaginatedResult<List<Artist>>> {

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
