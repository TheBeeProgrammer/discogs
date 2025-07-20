package com.clara.clarachallenge.ui.viewmodel.fakes

import androidx.paging.PagingData
import com.clara.domain.model.Album
import com.clara.domain.usecase.base.ExecutableUseCase
import com.clara.domain.usecase.model.UseCaseResult
import kotlinx.coroutines.flow.Flow

class FakeGetArtistAlbumsUseCase : ExecutableUseCase<Int, UseCaseResult<Flow<PagingData<Album>>>> {
    var result: UseCaseResult<Flow<PagingData<Album>>>? = null
    var receivedId: Int? = null

    override suspend fun invoke(params: Int): UseCaseResult<Flow<PagingData<Album>>> {
        receivedId = params
        return result ?: error("Fake result not set")
    }
}
