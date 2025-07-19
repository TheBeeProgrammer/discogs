package com.clara.data.common.repositories

import com.clara.domain.usecase.model.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class BaseRepository {

    /**
     * Executes a suspending block of code safely, catching any exceptions and returning a [UseCaseResult].
     *
     * @param block The suspend function to execute.
     * @return A [UseCaseResult] object, which will be [UseCaseResult.Success] if the block executes without exceptions,
     * or [UseCaseResult.Failure] if an exception is caught.
     */
    protected suspend fun <T> safeCall(
        block: suspend () -> T
    ): UseCaseResult<T> {
        return try {
            UseCaseResult.Success(block())
        } catch (e: Exception) {
            UseCaseResult.Failure(UseCaseResult.Reason.Unknown(e.message ?: "Unknown error"))
        }
    }

    /**
     * Emits a single [UseCaseResult] in a Flow, wrapping a suspending operation.
     */
    protected fun <T> safeFlowCall(
        block: suspend () -> T
    ): Flow<UseCaseResult<T>> = flow {
        try {
            emit(UseCaseResult.Success(block()))
        } catch (e: Exception) {
            emit(UseCaseResult.Failure(UseCaseResult.Reason.Unknown(e.message ?: "Unknown error")))
        }
    }
}