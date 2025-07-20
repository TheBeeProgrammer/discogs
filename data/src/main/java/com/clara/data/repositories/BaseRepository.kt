package com.clara.data.repositories

import com.clara.data.api.ApiConstants.NOT_FOUND_CODE
import com.clara.domain.model.NetworkUnavailableException
import com.clara.domain.usecase.model.UseCaseResult
import retrofit2.HttpException
import java.net.SocketTimeoutException

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
        } catch (e: HttpException) {
            when (e.code()) {
                NOT_FOUND_CODE -> UseCaseResult.Failure(UseCaseResult.Reason.NotFound)
                else -> UseCaseResult.Failure(UseCaseResult.Reason.Unknown(e.message()))
            }
        } catch (_: NetworkUnavailableException) {
            UseCaseResult.Failure(UseCaseResult.Reason.NoInternet)
        } catch (_: SocketTimeoutException) {
            UseCaseResult.Failure(UseCaseResult.Reason.Timeout)
        } catch (e: Exception) {
            UseCaseResult.Failure(UseCaseResult.Reason.Unknown(e.message ?: "Unknown error"))
        }
    }
}