package com.clara.domain.usecase

import com.clara.domain.model.ArtistDetail
import com.clara.domain.repositories.ArtistDetailRepository
import com.clara.domain.usecase.base.ExecutableUseCase
import com.clara.domain.usecase.model.UseCaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArtistDetailUseCase @Inject constructor(
    private val repository: ArtistDetailRepository
) : ExecutableUseCase<Int, UseCaseResult<Flow<ArtistDetail>>> {
    override suspend fun invoke(params: Int): UseCaseResult<Flow<ArtistDetail>> {
        return repository.getArtistDetail(params)
    }
}