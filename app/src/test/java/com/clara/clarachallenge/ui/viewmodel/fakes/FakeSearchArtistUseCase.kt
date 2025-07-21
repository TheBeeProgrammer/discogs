package com.clara.clarachallenge.ui.viewmodel.fakes

import androidx.paging.PagingData
import com.clara.domain.model.Artist
import com.clara.domain.usecase.base.ExecutableUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Fake implementation of [ExecutableUseCase] for testing purposes.
 * This class simulates the behavior of searching for artists.
 *
 * @property lastReceivedQuery The last query received by the use case.
 * @property searchResult The flow of [PagingData] representing the search results.
 * @property callCount The number of times the use case has been invoked.
 */
class FakeSearchArtistUseCase : ExecutableUseCase<String, Flow<PagingData<Artist>>> {
    var lastReceivedQuery: String = ""
    var searchResult: Flow<PagingData<Artist>> = flowOf(PagingData.empty())
    var callCount: Int = 0

    override suspend fun invoke(params: String): Flow<PagingData<Artist>> {
        callCount++
        lastReceivedQuery = params
        return searchResult
    }
}