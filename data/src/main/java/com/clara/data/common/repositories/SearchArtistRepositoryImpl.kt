package com.clara.data.common.repositories

import com.clara.data.common.mapper.Mapper
import com.clara.data.remote.DiscogsApiService
import com.clara.data.remote.entities.ApiArtistSearchResponse
import com.clara.domain.SearchArtistRepository
import com.clara.domain.model.Artist
import com.clara.domain.model.PaginatedResult
import com.clara.domain.model.Pagination
import com.clara.domain.usecase.model.UseCaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchArtistRepositoryImpl @Inject constructor(
    private val apiService: DiscogsApiService,
    private val mapper: Mapper<ApiArtistSearchResponse, PaginatedResult<List<Artist>>>
) : SearchArtistRepository, BaseRepository() {

    override suspend fun searchArtist(
        query: String,
        pagination: Pagination
    ): Flow<UseCaseResult<PaginatedResult<List<Artist>>>> = safeFlowCall {
        val apiResponse = apiService.searchArtists(
            query = query,
            page = pagination.page,
            perPage = pagination.pages
        )
        mapper.map(apiResponse)
    }
}
