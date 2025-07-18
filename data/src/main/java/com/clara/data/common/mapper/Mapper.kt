package com.clara.data.common.mapper

import com.clara.data.remote.entities.ApiResult
import com.clara.data.remote.entities.DiscogsError
import com.clara.domain.usecase.model.UseCaseResult

/**
 * A generic interface for mapping objects from the data layer to the domain layer.
 *
 * This abstraction helps decouple external data representations (e.g., API responses or
 * database entities) from the business models by transforming them into domain-specific types.
 *
 * @param FROM The type coming from the data layer (e.g., DTO, Entity).
 * @param TO The type used in the domain layer (e.g., Domain model).
 */
interface Mapper<FROM, TO> {
    /**
     * Maps an object of type [FROM] data layer.
     * into an object of type [TO] domain layer.
     *
     * @param from The input object from the data layer.
     * @return The mapped object in the domain model format.
     */
    fun map(from: FROM): TO
}

/**
 * Maps an [ApiResult] from the data layer into a [UseCaseResult] in the domain layer,
 * using the provided [mapper] to convert the success payload.
 *
 * This function bridges the gap between the network result types and domain result types,
 * abstracting away error handling and mapping logic from the domain.
 *
 * @param mapper A [Mapper] that defines how to transform the successful payload from [FROM] to [TO].
 * @return A [UseCaseResult] representing either a successful mapped value or a domain-level failure.
 */
fun <FROM, TO> ApiResult<FROM>.apiResultToResultDomain(mapper: Mapper<FROM, TO>): UseCaseResult<TO> {
    return when (this) {
        is ApiResult.Success -> UseCaseResult.Success(mapper.map(this.data))
        is ApiResult.Error -> {
            val reason = when (val err = error) {
                is DiscogsError.ApiError -> {
                    when (err.code) {
                        404 -> UseCaseResult.Reason.NotFound
                        401 -> UseCaseResult.Reason.Unauthorized
                        else -> UseCaseResult.Reason.Unknown(err.message)
                    }
                }

                is DiscogsError.NetworkError.NoInternet -> UseCaseResult.Reason.NoInternet
                is DiscogsError.NetworkError.Timeout -> UseCaseResult.Reason.Timeout
            }
            UseCaseResult.Failure(reason)
        }
    }
}
