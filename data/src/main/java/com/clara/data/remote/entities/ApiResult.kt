package com.clara.data.remote.entities

/**
 * Represents the result of an API call.
 *
 * This sealed class encapsulates the possible outcomes of an API request:
 * - [Success]: Indicates a successful response, containing the [data], a flag indicating if the [data] is [fromCache], and optional [metadata].
 * - [Error]: Indicates an error occurred during the request, containing the [error] details and optional [cachedData].
 *
 * @param T The type of data expected in a successful response.
 */
sealed class ApiResult<out T> {
    data class Success<out T>(
        val data: T,
        val metadata: Metadata? = null
    ) : ApiResult<T>()

    data class Error(
        val error: DiscogsError,
    ) : ApiResult<Nothing>()

    data class Metadata(
        val page: Int,
        val totalPages: Int,
        val lastUpdated: Long? = null
    )
}

sealed class DiscogsError {
    data class ApiError(
        val code: Int,
        val message: String,
        val details: Map<String, String>? = null
    ) : DiscogsError()

    sealed class NetworkError : DiscogsError() {
        object NoInternet : NetworkError()
        object Timeout : NetworkError()
    }
}