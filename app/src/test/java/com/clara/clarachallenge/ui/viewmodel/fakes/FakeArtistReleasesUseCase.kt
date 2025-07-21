package com.clara.clarachallenge.ui.viewmodel.fakes

import androidx.paging.PagingData
import com.clara.domain.model.Releases
import com.clara.domain.usecase.base.ExecutableUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

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
