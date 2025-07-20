package com.clara.clarachallenge.ui.viewmodel.fakes

import androidx.paging.PagingData
import com.clara.domain.model.Artist
import com.clara.domain.usecase.base.ExecutableUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

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