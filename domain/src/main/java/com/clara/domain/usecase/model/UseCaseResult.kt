package com.clara.domain.usecase.model

sealed class UseCaseResult<out T> {
    data class Success<out T>(val data: T): UseCaseResult<T>()
    data class Failure(val reason: Reason): UseCaseResult<Nothing>()

    sealed class Reason {
        object NotFound: Reason()
        object Unauthorized: Reason()
        object NoInternet: Reason()
        object Timeout: Reason()
        data class Unknown(val message: String): Reason()
    }
}
