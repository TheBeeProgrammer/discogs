package com.clara.clarachallenge.ui.common

import com.clara.domain.usecase.model.UseCaseResult

/**
 * Converts a [UseCaseResult.Reason] to a user-friendly UI message.
 *
 * This extension function maps different reasons for a use case failure
 * to appropriate strings that can be displayed to the user.
 *
 * @return A [String] representing the UI message for the reason.
 */
fun UseCaseResult.Reason.toUiMessage(): String = when (this) {
    UseCaseResult.Reason.Unauthorized -> "Unauthorized access"
    UseCaseResult.Reason.NoInternet -> "No internet connection"
    UseCaseResult.Reason.Timeout -> "Timeout"
    is UseCaseResult.Reason.Unknown -> this.message
}
