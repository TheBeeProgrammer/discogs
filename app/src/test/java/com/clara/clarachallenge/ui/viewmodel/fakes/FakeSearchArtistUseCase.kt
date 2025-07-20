package com.clara.clarachallenge.ui.viewmodel.fakes

import androidx.paging.PagingData
import com.clara.domain.model.Artist
import com.clara.domain.usecase.base.ExecutableUseCase
import com.clara.domain.usecase.model.UseCaseResult
import kotlinx.coroutines.flow.Flow

class FakeSearchArtistUseCase : ExecutableUseCase<String, UseCaseResult<Flow<PagingData<Artist>>>> {
    var result: UseCaseResult<Flow<PagingData<Artist>>>? = null
    var receivedQuery: String? = null

    override suspend fun invoke(params: String): UseCaseResult<Flow<PagingData<Artist>>> {
        receivedQuery = params
        return result ?: error("Fake result not set")
    }
}
