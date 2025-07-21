package com.clara.clarachallenge.ui.viewmodel.fakes

import androidx.paging.PagingData
import com.clara.domain.model.Releases
import com.clara.domain.usecase.base.ExecutableUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Fake implementation of [ExecutableUseCase] for testing purposes.
 * This class simulates the behavior of fetching artist releases.
 *
 * @property searchResult The flow of PagingData representing the releases to be returned. Defaults to an empty PagingData.
 * @property lastReceivedArtistId The ID of the artist that was last received as a parameter. Defaults to -1.
 * @property callCount The number of times the [invoke] method has been called.
 */
class FakeArtistReleasesUseCase : ExecutableUseCase<Int, Flow<PagingData<Releases>>> {
    var searchResult: Flow<PagingData<Releases>> = flowOf(PagingData.empty())
    var lastReceivedArtistId: Int = -1
    var callCount: Int = 0

    override suspend fun invoke(params: Int): Flow<PagingData<Releases>> {
        callCount++
        lastReceivedArtistId = params
        return searchResult
    }
}
