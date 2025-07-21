package com.clara.clarachallenge.ui.viewmodel.fakes

import com.clara.domain.model.ArtistDetail
import com.clara.domain.usecase.base.ExecutableUseCase
import com.clara.domain.usecase.base.UseCaseResult
import kotlinx.coroutines.flow.Flow

/**
 * A fake implementation of the `ExecutableUseCase` for testing purposes.
 * This class simulates the behavior of a use case that retrieves artist details.
 *
 * @property result The result to be returned by the use case. It can be set to simulate different scenarios.
 * @property receivedId The ID received by the use case when invoked.
 */
class FakeArtistDetailUseCase : ExecutableUseCase<Int, UseCaseResult<Flow<ArtistDetail>>> {
    var result: UseCaseResult<Flow<ArtistDetail>>? = null
    var receivedId: Int? = null

    override suspend fun invoke(params: Int): UseCaseResult<Flow<ArtistDetail>> {
        receivedId = params
        return result ?: error("Fake result not set")
    }
}

