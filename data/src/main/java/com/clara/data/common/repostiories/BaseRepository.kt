package com.clara.data.common.repostiories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Base class for repositories providing common utility functions for safe API calls.
 *
 * This class offers methods to wrap suspend functions and Flow emissions in a `Result` type,
 * handling potential exceptions and simplifying error management in the data layer.
 */
abstract class BaseRepository {

    /**
     * Executes a suspending block of code safely, catching any exceptions and returning a [Result].
     *
     * This function is useful for wrapping suspend functions that might throw exceptions,
     * allowing for cleaner error handling.
     *
     * @param T The type of the successful result.
     * @param block The suspend function to execute.
     * @return A [Result] object, which will be [Result.success] if the block executes without exceptions,
     * or [Result.failure] if an exception is caught.
     */
    protected suspend fun <T> safeCall(
        block: suspend () -> T
    ): Result<T> {
        return try {
            Result.success(block())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Wraps a suspending block in a Flow that emits a [Result].
     *
     * This function is designed to safely execute a suspending operation and emit its outcome
     * (success or failure) as a [Result] within a Flow. If the block executes successfully,
     * it emits `Result.success` with the returned value. If an exception occurs during the
     * execution of the block, it emits `Result.failure` with the caught exception.
     *
     * This is useful for handling operations that might fail in a reactive way, allowing
     * downstream collectors to react to either success or failure.
     *
     * @param T The type of the value returned by the suspending block.
     * @param block The suspending lambda function to execute.
     * @return A [kotlinx.coroutines.flow.Flow] that emits a single [Result] object,
     *         representing either the successful result of the block or the exception thrown.
     */
    protected fun <T> safeFlowCall(
        block: suspend () -> T
    ): Flow<Result<T>> = flow {
        try {
            emit(Result.success(block()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}