package com.clara.domain.usecase.base

import kotlinx.coroutines.flow.Flow

/**
 * Interface for a Use Case (Interactor in terms of Clean Architecture) returning a [kotlinx.coroutines.flow.Flow]
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 *
 * Use cases are the entry points to the domain layer.
 *
 * @param Params the type of the input parameters.
 * @param Result the type of the result.
 */
interface FlowExecutableUseCase<in Params, out Result> {
    operator fun invoke(parameters: Params): Flow<Result>
}