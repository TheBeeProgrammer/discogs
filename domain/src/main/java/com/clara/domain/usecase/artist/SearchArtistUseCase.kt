package com.clara.domain.usecase.artist

import androidx.paging.PagingData
import com.clara.domain.model.Artist
import com.clara.domain.repositories.SearchArtistRepository
import com.clara.domain.usecase.base.ExecutableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for searching artists.
 * This class encapsulates the business logic for searching artists.
 * It uses a [com.clara.domain.repositories.SearchArtistRepository] to fetch the data.
 *
 * @property repository The repository for fetching artist data.
 */
class SearchArtistUseCase @Inject constructor(
    private val repository: SearchArtistRepository
) : ExecutableUseCase<String, Flow<PagingData<Artist>>> {
    override suspend fun invoke(params: String): Flow<PagingData<Artist>> {
        return repository.searchArtist(params)
    }
}