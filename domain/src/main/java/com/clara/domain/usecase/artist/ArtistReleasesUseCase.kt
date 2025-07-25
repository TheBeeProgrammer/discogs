package com.clara.domain.usecase.artist

import androidx.paging.PagingData
import com.clara.domain.model.Releases
import com.clara.domain.repositories.ArtistReleasesRepository
import com.clara.domain.usecase.base.ExecutableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving artist releases.
 *
 * This use case fetches a paginated flow of releases for a given artist ID.
 *
 * @param repository The repository responsible for fetching artist releases.
 */
class ArtistReleasesUseCase @Inject constructor(
    private val repository: ArtistReleasesRepository
) : ExecutableUseCase<Int, Flow<PagingData<Releases>>> {
    override suspend fun invoke(params: Int): Flow<PagingData<Releases>> {
        return repository.getArtistReleases(params)
    }

}