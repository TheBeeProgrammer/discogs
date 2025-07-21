package com.clara.clarachallenge.ui.viewmodel.fakes

import com.clara.domain.model.ArtistDetail
import com.clara.domain.usecase.base.ExecutableUseCase
import com.clara.domain.usecase.base.UseCaseResult
import kotlinx.coroutines.flow.Flow

class FakeArtistDetailUseCase : ExecutableUseCase<Int, UseCaseResult<Flow<ArtistDetail>>> {
    var result: UseCaseResult<Flow<ArtistDetail>>? = null
    var receivedId: Int? = null

    override suspend fun invoke(params: Int): UseCaseResult<Flow<ArtistDetail>> {
        receivedId = params
        return result ?: error("Fake result not set")
    }
}

