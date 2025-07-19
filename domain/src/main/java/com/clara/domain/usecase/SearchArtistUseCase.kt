package com.clara.domain.usecase

import com.clara.domain.SearchArtistRepository
import com.clara.domain.model.Artist
import com.clara.domain.model.PaginatedResult
import com.clara.domain.usecase.base.FlowExecutableUseCase
import com.clara.domain.usecase.model.SearchArtistParams
import com.clara.domain.usecase.model.UseCaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchArtistUseCase @Inject constructor(
    private val repository: SearchArtistRepository
) : FlowExecutableUseCase<SearchArtistParams, UseCaseResult<PaginatedResult<List<Artist>>>> {

    override suspend fun invoke(parameters: SearchArtistParams): Flow<UseCaseResult<PaginatedResult<List<Artist>>>> {
        return repository.searchArtist(parameters.query, parameters.pagination)
    }
}
