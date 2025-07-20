package com.clara.domain.usecase.artist

import com.clara.domain.model.ArtistDetail
import com.clara.domain.repositories.ArtistDetailRepository
import com.clara.domain.usecase.base.ExecutableUseCase
import com.clara.domain.usecase.base.UseCaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for fetching the details of a specific artist.
 *
 * This class encapsulates the business logic for retrieving artist details.
 * It interacts with the [com.clara.domain.repositories.ArtistDetailRepository] to get the data.
 *
 * @param repository The repository responsible for fetching artist details.
 */
class ArtistDetailUseCase @Inject constructor(
    private val repository: ArtistDetailRepository
) : ExecutableUseCase<Int, UseCaseResult<Flow<ArtistDetail>>> {
    override suspend fun invoke(params: Int): UseCaseResult<Flow<ArtistDetail>> {
        return repository.getArtistDetail(artistId = params)
    }
}